import os
from groq import Groq

#API
GROQ_API_KEY = os.getenv('API_Qroq')

# Crear cliente de Groq
client = Groq(api_key=GROQ_API_KEY)

puestos = "Producción, Mantenimiento técnico, Ensamblaje de maquinaria, Control de calidad, Seguridad ocupacional, Operaciones industriales, Desarrollo de software, Soporte técnico, Inteligencia artificial, Ciberseguridad, Bases de datos, UX/UI, Contabilidad, Gestión de proyectos, Logística empresarial, Estrategia de ventas, Análisis financiero, Marketing digital, Arquitectura, Albañilería, Electricidad de obra, Carpintería, Diseño estructural, Maquinaria pesada, Conducción de carga, Mensajería, Aviación, Mantenimiento vehicular, Transporte marítimo, Gestión de almacenes, Medicina general, Enfermería, Psicología clínica, Terapia física, Nutrición, Odontología, Docencia escolar, Tutorías especializadas, Educación online, Investigación académica, Traducción e interpretación, Capacitación empresarial, Ilustración, Producción audiovisual, Modelado 3D, Dirección de arte, Edición de video, Fotografía profesional, Actuación, Locución, Guionismo, Gestión de eventos, Creación de contenido, Producción cinematográfica, Atención al cliente, Supervisión de tiendas, E-commerce, Gestión de inventarios, Importación y exportación, Ventas corporativas, Cocina profesional, Sommelier, Guía turístico, Gestión hotelera, Catering, Repostería artesanal, Derecho corporativo, Propiedad intelectual, Asesoría legal, Notaría, Mediación de conflictos, Derecho penal, Agricultura sostenible, Energías renovables, Ingeniería ambiental, Biotecnología, Pesca industrial, Forestación, Limpieza profesional, Jardinería, Cuidado de personas mayores, Estilismo y belleza, Organización de espacios, Reparaciones domésticas, Biotecnología, Astronomía, Física aplicada, Ingeniería genética, Investigación médica, Desarrollo de materiales."

def generate_puesto(frase):
    #crear guion y palabras con Groq
    chat_completion = client.chat.completions.create(
            messages=[
                {
                    "role": "system",
                    "content": "Eres un bot que apartir de un anuncio de trabajo respondes la siguiente pregunta: ¿A que categoria de puesto de trabajo se relaciona más el anuncio? Las categorias son: "+puestos+" Solo puedes responder con una unica categoria",
                },
                {
                    "role": "user",
                    "content": frase,
                }
            ],
            model="llama-3.3-70b-versatile",
        )

    response = chat_completion.choices[0].message.content

    return response