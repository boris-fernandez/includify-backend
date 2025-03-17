from flask import Flask, jsonify, request
import shutil
from moviepy.video.fx import all as vfx
import re
import os
from groq import Groq
import requests
import random
import edge_tts
import asyncio
import moviepy.editor as mp
import ffmpeg
import textwrap
import subprocess
import json
from moviepy.editor import VideoFileClip, AudioFileClip, concatenate_videoclips, vfx

#APIS
GROQ_API_KEY = os.getenv('API_Qroq')
API_PIXABAY = os.getenv('API_Pixabay')

#Variables predefinidas
subtitle_font_path = "Bungee-Regular"
max_chars_per_line = 20
max_subtitle_lines = 11
subtitle_font_size = 70
subtitle_vertical_spacing = 120
final_video_width = 1080
final_video_height = 1920
lineasGuion = []
palabrasGuion = []

# Carpetas temporales
folderAudios = r"C:\Users\josue\OneDrive - Universidad Tecnologica del Peru\Desktop\INCLUDIFY2.0\flaskAPI\voz"
folderVideos = r"C:\Users\josue\OneDrive - Universidad Tecnologica del Peru\Desktop\INCLUDIFY2.0\flaskAPI\media"
# Ruta de salida del video final
finalVideo = r"C:\Users\josue\OneDrive - Universidad Tecnologica del Peru\Desktop\INCLUDIFY2.0\flaskAPI\video_final.mp4"

# Crear directorio si no existe
if not os.path.exists(folderAudios):
    os.makedirs(folderAudios)

# Crear directorio si no existe
if not os.path.exists(folderVideos):
    os.makedirs(folderVideos)

# Voces disponibles
voices = [
    "es-MX-DaliaNeural",   # Español México, Femenino
    "es-MX-JorgeNeural",   # Español México, Masculino
    "es-ES-AlvaroNeural",  # Español España, Masculino
    "es-ES-ElviraNeural",  # Español España, Femenino
    "es-CO-GonzaloNeural", # Español Colombia, Masculino
    "es-CO-ElenaNeural",   # Español Colombia, Femenino
    "es-AR-ElenaNeural"    # Español Argentina, Femenino
]

# Crear cliente de Groq
client = Groq(api_key=GROQ_API_KEY)

async def text_to_speech(text, output_file, voice):
    """Convierte texto a voz y guarda el archivo de audio."""
    communicate = edge_tts.Communicate(text, voice)
    await communicate.save(output_file)
    print(f"Audio guardado en: {output_file}")

async def create_audio():
    """Procesa la lista de frases y genera los audios."""
    selected_voice = random.choice(voices)  # Seleccionar una voz aleatoria
    print(f"Usando la voz: {selected_voice}")

    tasks = []
    for i, frase in enumerate(lineasGuion, start=1):
        output_audio = os.path.join(folderAudios, f"frase{i}.mp3")
        tasks.append(text_to_speech(frase, output_audio, selected_voice))

    await asyncio.gather(*tasks)  # Ejecutar todas las conversiones en paralelo
    print("Todos los audios han sido generados exitosamente.")

def extract_number(filename):
    match = re.search(r'\d+', filename)
    return int(match.group()) if match else 0

def create_guion_groq(anuncio):
    #crear guion y palabras con Groq
    chat_completion = client.chat.completions.create(
            messages=[
                {
                    "role": "system",
                    "content": "Eres un bot que a partir de texto con indicaciones sobre un puesto de trabajo creas un texto con 10 lines no numeradas ni marcadas de ninguna forma el texto en conjunto tiene que ser una invitación a trabajar en esta empresa, tienes que describir el puesto de trabajo e invitar a unirse con el fin de llamar la atención, la idea es que cuentes la información más necesaria y relevante del puesto de trabajo. Luego a esa misma respuesta le agregas de la linea 11 a 20, 10 palabaras que cada palabra describa lo principal del texto original, es decir la linea 11 es una palabra que es la idea principal de la linea 1 la linea 12 de la lina 2 y así Tienen que ser palabras faciles y comunes",
                },
                {
                    "role": "user",
                    "content": anuncio,
                }
            ],
            model="llama-3.3-70b-versatile",
        )

    guion = chat_completion.choices[0].message.content
    return guion

def videos_pixabay():
    # Descargar videos de Pixabay
    for i, query in enumerate(palabrasGuion[:10], start=1):  # Solo 10 videos
        file_name = os.path.join(folderVideos, f"video{i}.mp4")
        
        video_url = f"https://pixabay.com/api/videos/?key={API_PIXABAY}&q={query}&video_type=all"
        response = requests.get(video_url)
        
        if response.status_code == 200:
            data = response.json()
            videos = data.get("hits", [])
            
            if not videos:
                print(f"No se encontraron videos para {query}, buscando video de tecnologia...")
                video_url = f"https://pixabay.com/api/videos/?key={API_PIXABAY}&q=tecnologia&video_type=all"
                response = requests.get(video_url)
                data = response.json()
                videos = data.get("hits", [])
                
            if videos:
                video = random.choice(videos)
                video_download_url = video['videos']['large']['url']
                
                print(f"Descargando video {i}: {video_download_url}")
                video_response = requests.get(video_download_url, stream=True)
                
                if video_response.status_code == 200:
                    with open(file_name, "wb") as file:
                        for chunk in video_response.iter_content(chunk_size=1024):
                            file.write(chunk)
                    print(f"✅ Video guardado como {file_name}")
                else:
                    print(f"❌ Error al descargar el video {i}")
            else:
                print(f"⚠️ No se encontraron videos ni siquiera de tecnología para {query}")
        else:
            print(f"❌ Error en la solicitud de videos para {query}: {response.status_code}")

def resize_videos():
    for filename in os.listdir(folderVideos):
        if filename.endswith(".mp4") and filename.strip():  # Evita archivos vacíos
            video_path = os.path.join(folderVideos, filename)

            # Generar un archivo temporal para evitar errores de escritura
            temp_output = os.path.join(folderVideos, f"temp_{filename}")

            # FFmpeg: escalar altura a 1920px y recortar el ancho al centro
            ffmpeg.input(video_path).filter(
                "scale", "-2", final_video_height  # Escala manteniendo la proporción
            ).filter(
                "crop", final_video_width, final_video_height  # Recorta el ancho
            ).output(
                temp_output, vcodec="libx264", crf=23, preset="slow"
            ).run(overwrite_output=True)

            # Reemplazar el archivo original con el nuevo
            os.replace(temp_output, video_path)

            print(f"✅ Video procesado correctamente: {filename}")

    print("🎉 Todos los videos han sido corregidos y reescalados a 1080x1920.")

def subtitle_videos():
    # Obtener los primeros 10 videos de la carpeta de origen
    video_files = sorted([f for f in os.listdir(folderVideos) if f.endswith(".mp4")],key=extract_number)[:10]

    # Procesar cada video con su subtítulo correspondiente
    for idx, video_name in enumerate(video_files):
        video_path = os.path.join(folderVideos, video_name)
        temp_output_path = os.path.join(folderVideos, f"temp_{video_name}")
        
        # Obtener la duración del video con FFmpeg
        ffprobe_command = [
            "ffprobe", "-v", "error", "-select_streams", "v:0",
            "-show_entries", "format=duration", "-of", "json", video_path
        ]
        result = subprocess.run(ffprobe_command, capture_output=True, text=True)
        video_duration = float(json.loads(result.stdout)["format"]["duration"]) if result.returncode == 0 else 5
        time_end = video_duration - 0.1
        
        # Obtener subtítulo formateado
        wrapped_text = textwrap.wrap(lineasGuion[idx], width=max_chars_per_line)
        text_fragments = [wrapped_text[k:k+max_subtitle_lines] for k in range(0, len(wrapped_text), max_subtitle_lines)]
        
        overlays = []
        for fragment in text_fragments:
            for j, line in enumerate(fragment):
                drawtext = (
                    f"drawtext=text='{line}':"
                    f"fontfile='{subtitle_font_path}':"
                    f"fontcolor=white:bordercolor=black:borderw=5:fontsize={subtitle_font_size}:"
                    f"x=(w-text_w)/2:y=(h-text_h)/2-{(len(fragment)-1) * subtitle_vertical_spacing/2} + {j * subtitle_vertical_spacing}:"
                    f"enable='lt(t,{time_end})'"
                )
                overlays.append(drawtext)
        
        # Crear el comando FFmpeg
        ffmpeg_command = [
            "ffmpeg", "-i", video_path,
            "-vf", ",".join(overlays),
            "-codec:a", "copy", temp_output_path
        ]
        
        # Ejecutar FFmpeg
        subprocess.run(ffmpeg_command)
        
        # Reemplazar el video original con el subtitulado
        os.replace(temp_output_path, video_path)
        print(f"✅ Video procesado: {video_path}")

    print("🚀 Todos los videos han sido procesados y subtitulados correctamente.")

def join_videos_with_audios():
    for i in range(1, 11):
        video_path = os.path.join(folderVideos, f"video{i}.mp4")
        audio_path = os.path.join(folderAudios, f"frase{i}.mp3")

        if os.path.exists(video_path) and os.path.exists(audio_path):
            # Cargar video y audio
            video = VideoFileClip(video_path)
            audio = AudioFileClip(audio_path)

            # Ajustar video a la duración del audio
            if video.duration > audio.duration:
                video = video.subclip(0, audio.duration)  # Recortar video si es más largo
            elif video.duration < audio.duration:
                factor = audio.duration / video.duration
                if factor < 1.5:
                    video = video.fx(vfx.speedx, factor)  # Ralentizar si es poco tiempo
                else:
                    loop_count = int(audio.duration // video.duration)
                    remainder = audio.duration % video.duration

                    video_loops = [video] * loop_count  # Repetir el video
                    if remainder > 0:
                        video_loops.append(video.subclip(0, remainder))  # Agregar lo que falta

                    video = concatenate_videoclips(video_loops, method="compose")  # Unir sin congelar frames

            # Forzar duración exacta para evitar congelamientos
            video = video.subclip(0, audio.duration)

            # Asegurar FPS sin efectos extra
            video = video.set_fps(30)

            # Agregar el audio al video
            video = video.set_audio(audio)

            # Guardar video final
            output_path = os.path.join(folderVideos, f"final_video{i}.mp4")
            video.write_videofile(output_path, codec="libx264", fps=30, threads=4)

            print(f"✅ Video finalizado: {output_path}")

    print("🎉 Todos los videos han sido procesados correctamente.")

def join_final_video():
    # Obtener la lista de archivos en orden
    nombres_videos = [f"final_video{i}.mp4" for i in range(1, 11)]
    rutas_videos = [os.path.join(folderVideos, nombre) for nombre in nombres_videos]

    # Cargar los videos
    clips = [VideoFileClip(video) for video in rutas_videos]

    # Unir los videos en secuencia
    video_final = concatenate_videoclips(clips, method="compose")

    # Guardar el resultado
    video_final.write_videofile(finalVideo, codec="libx264", fps=24)

    # Cerrar los clips para liberar memoria
    for clip in clips:
        clip.close()

def limpiar_carpetas():
    for folder in [folderAudios, folderVideos]:
        if os.path.exists(folder):
            # Eliminar archivos dentro de la carpeta
            for archivo in os.listdir(folder):
                archivo_path = os.path.join(folder, archivo)
                if os.path.isfile(archivo_path):
                    os.remove(archivo_path)  # Elimina archivos
                elif os.path.isdir(archivo_path):
                    shutil.rmtree(archivo_path)  # Elimina subcarpetas

def generate_video(data):
    #Obtener guion de 10 lineas con Qroq
    print("Generando guion...")
    guion = create_guion_groq(data['anuncio'])
    guionCompleto = ""

    #Separar guion en listas de lineas y palabras
    print("Separando guion...")
    contenido = 0
    for line in guion.split("\n"):
        if line != "":
            if contenido < 10:
                lineasGuion.append(line)
                guionCompleto=guionCompleto+line
                contenido+=1
            elif contenido < 20 and contenido >= 10:
                palabrasGuion.append(line)
                contenido+=1
    # print(lineasGuion)
    # print(palabrasGuion)
    print(guionCompleto)

    # Crear audios del guion
    print("Creando audios...")
    asyncio.run(create_audio())

    # Descargar videos de Pixabay
    print("Descargando videos de Pixabay...")
    videos_pixabay()

    # RESIZE VIDEOS
    print("Redimensionando videos...")
    resize_videos()
    
    #SUBTITULAR VIDEOS
    print("Subtitulando videos...")
    subtitle_videos()

    #unir videos con audios
    print("Uniendo videos con audios...")
    join_videos_with_audios()
    
    #unir los 10 videos y guardar el video final
    print("Uniendo videos...")
    join_final_video()
    print("Video final guardado correctamente.")
    return jsonify({"message": guionCompleto})