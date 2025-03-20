import traceback
import time
from flask import Flask, jsonify, request
import videoTiktok
import videoSeñas
import califications
import publicVideo
import createPDF
import categoriasTrabajo

app = Flask(__name__)

# Endpoint POST
@app.route('/generateVideo', methods=['POST'])
def create_video():
    inicio = time.time()  # Captura el tiempo de inicio
    
    data = request.get_json()

    categoriaPuesto = categoriasTrabajo.generate_puesto(data["anuncio"])
    
    videoTiktok.limpiar_carpetas()

    respuesta = videoTiktok.generate_video(data)

    guionCompleto = respuesta.get_json()["message"]  # <--- ✅ Guion del video

    videoSeñas.generate_video_signs(guionCompleto)

    videoTiktok.limpiar_carpetas()

    urlVideoTiktok = publicVideo.upload_video("Tiktok")  # <--- ✅ Url del video de Tiktok
    urlSeñas = publicVideo.upload_video("Señas")  # <--- ✅ Url del video de Señas
    score = califications.generate_score(data)  # <--- ✅ Lista que guarda las calificaciones

    fin = time.time()  # Captura el tiempo de finalización

    respuesta = jsonify({
        "guion_video": guionCompleto,
        "video": urlVideoTiktok,
        "video_señas": urlSeñas,
        "calificaciones": score,
        "categoria": categoriaPuesto,
    })

    print("programa terminado en "+str(fin-inicio)+" segundos")

    return respuesta, 201

# Endpoint POST
@app.route('/generatePdf', methods=['POST'])
def create_cv():
    try:
        # Intentar obtener JSON
        data = request.get_json()

        # Validar que todos los campos estén presentes
        required_fields = ["respuestas", "categoria", "nombre", "apellido", "telefono", "correo"]
        missing_fields = [field for field in required_fields if field not in data]

        if missing_fields:
            return jsonify({"error": "Faltan campos obligatorios", "missing_fields": missing_fields}), 400

        lista = data["respuestas"]
        lista = [int(x) for x in lista]

        # Extraer valores
        r1, r2, r3, r4, r5, r6, r7, r8, r9, r10 = lista[0], lista[1], lista[2], lista[3], lista[4], lista[5], lista[6], lista[7], lista[8], lista[9]
        trabajo, nombre, apellido, telefono, correo = data["categoria"], data["nombre"], data["apellido"], data["telefono"], data["correo"]

        # Generar PDF
        url = createPDF.generate_pdf(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, trabajo, nombre, apellido, telefono, correo)
        
        respuesta = jsonify({
            "pdf_url": url,
        })

        return respuesta, 201

    except Exception as e:
        print(traceback.format_exc())  # 🔍 Esto imprimirá el error completo en la consola de Render
        return jsonify({"error": "Ocurrió un error en el servidor", "detalle": str(e)}), 500

    
# Endpoint POST
@app.route('/match', methods=['POST'])
def match_users():
    
    pass

if __name__ == '__main__':
    app.run(debug=True)
