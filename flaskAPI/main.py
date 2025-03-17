import time
from flask import Flask, jsonify, request
import videoTiktok
import videoSeñas

app = Flask(__name__)

# Endpoint POST
@app.route('/generateVideo', methods=['POST'])
def create_video():
    inicio = time.time()  # Captura el tiempo de inicio
    
    data = request.get_json()
    
    videoTiktok.limpiar_carpetas()
    respuesta = videoTiktok.generate_video(data)
    guionCompleto = respuesta.get_json()["message"]
    videoSeñas.generate_video_signs(guionCompleto)

    videoTiktok.limpiar_carpetas()

    fin = time.time()  # Captura el tiempo de finalización

    return "programa terminado en "+str(fin-inicio), 201

    #Guardar video
    #Guardar videoSeñas
    #Guardar etiquetas en tabla

if __name__ == '__main__':
    app.run(debug=True)
