import os
import cloudinary
import cloudinary.uploader
from groq import Groq
from flask import jsonify
import convertapi

# Configuration       
cloudinary.config( 
    cloud_name = os.getenv("CLOUDINARY_CLOUD_NAME"), 
    api_key = os.getenv("CLOUDINARY_API_KEY"),
    api_secret = os.getenv("CLOUDINARY_API_SECRET"),
    secure=True
)

#Api key
GROQ_API_KEY = os.getenv('API_Qroq')

options = {
    "margin-top": "0mm",
    "margin-right": "0mm",
    "margin-bottom": "0mm",
    "margin-left": "0mm",
    "page-width": "210mm",  # Ancho de una hoja A4
    "page-height": "297mm",  # Alto de una hoja A4
    "no-outline": None,  # Evita bordes en el renderizado
}

# Cliente de Groq
client = Groq(api_key=GROQ_API_KEY)

preguntas = [
    "Prefiero trabajar",
    "Mi nivel de experiencia es",
    "Busco una jornada laboral de",
    "Como persona con discapacidad, la accesibilidad en el trabajo",
    "Mi nivel de formación es",
    "Para ejercer en un entorno estructurado y de trabajo en equipo",
    "Sobre las herramientas de accesibilidad",
    "Sobre las adaptaciones en el puesto de trabajo",
    "Con respecto a los beneficios en el puesto de trabajo",
    "Sobre las tecnologías o habilidades específicas que tengo"
]

p1 = [  # Modalidad
    "de forma remota.",  
    "de manera presencial."
]

p2 = [  # Experiencia
    ", no tengo experiencia laboral.",  
    ", tengo formación, pero aún no experiencia laboral.",  
    ", he trabajado entre 1 y 2 años.",  
    ", con más de 2 años de experiencia laboral."
]

p3 = [  # Jornada laboral
    "tiempo completo.",  
    "medio tiempo.",  
    "horario flexible.",  
    "trabajos por proyectos o en modalidad freelance."
]

p4 = [  # Accesibilidad
    "no es un factor determinante para mí.",  
    "sería útil, pero no es indispensable.",  
    "es necesaria en ciertas condiciones para desempeñarme mejor.",  
    "es esencial, ya que requiero un entorno completamente accesible."
]

p5 = [  # Nivel de estudios
    "inexistente, no cuento con formación académica formal.",  
    "técnico, ya que poseo formación en este nivel.",  
    "universitario, pues tengo estudios en curso o finalizados.",  
    "avanzado, ya que cuento con una especialización, posgrado o estudios superiores."
]

p6 = [  # Entorno y ambiente
    "estructurado, pues necesito estabilidad y organización.",  
    "estándar, ya que puedo adaptarme a un entorno común.",  
    "cómodo, ya que prefiero un ambiente flexible.",  
    "dinámico, pues puedo trabajar eficientemente en entornos de alto rendimiento."
]

p7 = [  # Herramientas de accesibilidad
    "no es necesaria, ya que no requiero soporte para herramientas de accesibilidad.",  
    "limitada, pues uso mis propias herramientas y solo necesito autorización para emplearlas.",  
    "moderada, ya que me gustaría recibir apoyo en su uso.",  
    "total, pues necesito que se me brinde soporte completo para accesibilidad."
]

p8 = [  # Adaptaciones en el puesto de trabajo
    "innecesarias, ya que no requiero adaptaciones para desempeñar mi trabajo.",  
    "opcionales, pues me gustaría contar con ajustes menores.",  
    "flexibles, ya que quisiera tener la posibilidad de solicitarlas si fueran necesarias.",  
    "imprescindibles, pues requiero un entorno completamente adaptado."
]

p9 = [  # Beneficios
    "no son una prioridad para mí.",  
    "solo busco un contrato temporal sin beneficios adicionales.",  
    "benficios estándar, ya que busco un contrato fijo con beneficios comunes.",  
    "beneficios inclusivos, pues considero importante contar con beneficios acordes a mi condición."
]

p10 = [  # Tecnologías y habilidades
    "nulo, ya que no tengo conocimientos en tecnologías ni habilidades específicas.",  
    "básico, pues poseo conocimientos en tecnologías y habilidades estándar.",  
    "intermedio, ya que cuento con experiencia en herramientas relacionadas con mi campo.",  
    "avanzado, pues domino tecnologías y habilidades especializadas."
]

respuestas = [p1,p2,p3,p4,p5,p6,p7,p8,p9,p10]

def consulta_groq(descripcion_usuario, frase):
    chat_completion = client.chat.completions.create(
            messages=[
                {
                    "role": "system",
                    "content": "Eres un bot que apartir de una descripcion de un usuario con discapacidad redactas como si fueras el, partes para un curriculum para un trabajo "+descripcion_usuario,
                },
                {
                    "role": "user",
                    "content": frase,
                }
            ],
            model="llama-3.3-70b-versatile",
        )

    frase = chat_completion.choices[0].message.content
    # print(frase)
    return frase

def generate_pdf(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, trabajo, nombre, apellido, telefono, correo):
    cal = [r1,r2,r3,r4,r5,r6,r7,r8,r9,r10]
    descripcion_usuario = "Mi nombre es " + nombre + " " + apellido + " y soy una persona con discapacidad. " + "Busco trabajo como "+ trabajo + ". Mi número de teléfono es " + telefono + " y mi correo electrónico es " + correo + ". "
    for i in range(len(preguntas)):
        descripcion_usuario += preguntas[i] + " " + respuestas[i][cal[i]] + " "
    
    SobreMi = consulta_groq(descripcion_usuario, "Describe un poco de ti sin entrar en detalles profesionales, describete como persona y genera un sobre mi para tu perfil profesional, unicamente devuelve la respusta sin usar cosas como Sobre mi: o texto simplificado ni nada similar, el texto no debe superar los 500 caracteres y debes responder intuyendo cosas apartir de la descripcion proporcionada no inventes cosas adicionales")
    nivel = consulta_groq(descripcion_usuario, "Describe que tipo de accesibilidad necesitas que tipo de apoyo o como necesitas el ambiente no entres en detalles de que herramientas extra o que discapacidad tienes, el texto no debe superar los 500 caracteres y debes responder intuyendo cosas apartir de la descripcion proporcionada no inventes cosas adicionales")
    herramientas = consulta_groq(descripcion_usuario, "Describe si necesitas herramientas de accesibilidad sin decir tu discapacidad, el texto no debe superar los 500 caracteres y debes responder intuyendo cosas apartir de la descripcion proporcionada no inventes cosas adicionales")
    adaptaciones = consulta_groq(descripcion_usuario, "Describe que adaptaciones para el puesto de trabajo necesitas, el texto no debe superar los 500 caracteres y debes responder intuyendo cosas apartir de la descripcion proporcionada no inventes cosas adicionales")

    html_content = """<!DOCTYPE html>
    <html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Currículum Vitae</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
                width: 100%;
                background-color: #f4f4f4;
                color: #333;
            }
            header {
                background: #0073e6;
                color: white;
                text-align: center;
                padding: 20px 0;
            }
            h1, h2, h3 {
                margin: 0;
            }
            .container {
                padding: 40px 10%;
            }
            .section {
                margin-bottom: 30px;
                padding: 20px;
                background: white;
                border-radius: 8px;
                box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
            }
            .contact, .profile {
                text-align: center;
                margin-top: 10px;
                font-size: 15px;
            }
            .skills ul, .experience ul {
                list-style-type: none;
                padding: 0;
            }
            .skills li, .experience li {
                margin-bottom: 5px;
            }
            .section h2 {
                border-bottom: 2px solid #0073e6;
                padding-bottom: 5px;
            }
        </style>
    </head>
    <body>
        <header>
            <h1>"""+nombre+" "+apellido+"""</h1>
            <p class="contact">Correo: """+correo+""" | Teléfono: """+telefono+"""</p>
        </header>
        
        <div class="container">
            <section class="section profile">
                <h2>Sobre Mi</h2>
                <p>"""+SobreMi+"""</p>
            </section>

            <section class="section education">
                <h2>Formación Académica</h2>
                <p><strong>Nivel de Formación:</strong> """+p5[r5].capitalize()+"""</p>
            </section>
            
            <section class="section experience">
                <h2>Experiencia Laboral</h2>
                <ul>
                    <li><strong>Puesto: </strong> """+trabajo.capitalize()+p2[r2].capitalize()+"""</li>
                    <p>"""+p10[r10].capitalize()+"""</p>
                </ul>
            </section>
            
            <section class="section accessibility">
                <h2>Adaptaciones y Accesibilidad</h2>
                <p><strong>Nivel de accesibilidad requerido:</strong> """+nivel+"""</p>
                <p><strong>Herramientas de accesibilidad necesarias:</strong> """+herramientas+"""</p>
                <p><strong>Adaptaciones en el puesto de trabajo:</strong> """+adaptaciones+"""</p>
            </section>
            
            <section class="section benefits">
                <h2>Beneficios y Condiciones Laborales</h2>
                <p><strong>Tipo de contrato deseado:</strong> """+p3[r3].capitalize()+"""</p>
                <p><strong>Beneficios inclusivos preferidos:</strong> """+p9[r9].capitalize()+"""</p>
            </section>
            
        </div>
    </body>
    </html>
    """

    # Guardar el archivo como HTML
    with open("pagina.html", "w", encoding="utf-8") as file:
        file.write(html_content)
    print("Archivo HTML generado exitosamente: pagina.html")




    # Code snippet is using the ConvertAPI Python Client: https://github.com/ConvertAPI/convertapi-python

    convertapi.api_credentials = 'secret_x3tc6l4mwSUDCe6T'
    # Convertir HTML a PDF
    convertapi.convert('pdf', {
        'File': 'pagina.html'  # Archivo HTML de entrada
    }, from_format='html').save_files('cv.pdf')


    response = cloudinary.uploader.upload("cv.pdf", resource_type="raw")

    # Obtener la URL del archivo subido
    pdf_url = response["secure_url"]
    
    return pdf_url, 220