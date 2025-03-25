import os
from groq import Groq

#API
GROQ_API_KEY = os.getenv('API_Qroq')

# Crear cliente de Groq
client = Groq(api_key=GROQ_API_KEY)

questions = [
    "¿El puesto es remoto o presencial?",
    "¿Cuál es el nivel de experiencia requerido?",
    "¿Qué tipo de jornada laboral ofrece el puesto?",
    "¿Qué nivel de accesibilidad ofrece el puesto, para personas con discapacidad?",
    "¿Qué nivel de formación requiere el puesto?",
    "¿Qué tan importante es el trabajo en equipo y la adaptación al cambio en el puesto?",
    "¿El puesto permite el uso de herramientas de accesibilidad?",
    "¿El puesto ofrece adaptaciones para los empleados?",
    "¿Qué tipo de contrato o beneficios ofrece el puesto?",
    "¿El puesto requiere conocimientos de tecnologías específicas?"
]

answer_choices = [
    "(0) Remoto, (1) Presencial",
    "(0) No se requiere experiencia, (1) Se aceptan perfiles con formación pero sin experiencia, (2) Se requiere experiencia de 1-2 años, (3) Se requiere experiencia avanzada",
    "(0) Tiempo completo, (1) Medio tiempo, (2) Horario flexible, (3) Solo por proyectos o freelance",
    "(0) No es un factor importante, (1) Sería útil, pero no indispensable, (2) Necesito ciertas condiciones accesibles para trabajar mejor, (3) Se requiere un entorno completamente accesible",
    "(0) No se requiere formación específica, (1) Se aceptan candidatos con formación técnica, (2) Carrera universitaria en curso o terminada, (3) Especialización, posgrado o más",
    "(0) No se garantiza un entorno estructurado, (1) Se ofrece un entorno estándar, (2) Se ofrece un ambiente cómodo, (3) Se prioriza un entorno de alto rendimiento",
    "(0) No se ofrece soporte para herramientas de accesibilidad, (1) Se permite pero no se brinda apoyo, (2) Se fomenta su uso, (3) Se brinda soporte total para accesibilidad",
    "(0) No se ofrecen adaptaciones, (1) Se pueden realizar ajustes menores, (2) Se ofrecen adaptaciones bajo solicitud, (3) Se garantiza un entorno adaptado",
    "(0) No se ofrecen beneficios, (1) Se ofrece contrato temporal, (2) Se ofrece contrato fijo con beneficios estándar, (3) Se ofrecen beneficios inclusivos",
    "(0) No se requieren tecnologías específicas, (1) Se requieren conocimientos básicos, (2) Se requieren conocimientos intermedios, (3) Se requieren conocimientos avanzados"
]

calificaciones=[]

def generate_score(data):
    for i in range(len(questions)):
        #crear guion y palabras con Groq
        chat_completion = client.chat.completions.create(
                messages=[
                    {
                        "role": "system",
                        "content": "Eres un bot que apartir de un anuncio de trabajo respondes la siguiente pregunta: "+questions[i]+", y devuelves el numero de la respuesta correcta. "+answer_choices[i]+", unicamente debes devolver el numero correspondiente sin parentesis ni texto adicional",
                    },
                    {
                        "role": "user",
                        "content": data['anuncio'],
                    }
                ],
                model="llama-3.3-70b-versatile",
            )

        response = chat_completion.choices[0].message.content
        # print("PREGUNTA: "+str(i+1))
        # print(response+"\n")
        calificaciones.append(response)
    return calificaciones