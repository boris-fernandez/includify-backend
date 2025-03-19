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
        "r1": score[0],
        "r2": score[1],
        "r3": score[2],
        "r4": score[3],
        "r5": score[4],
        "r6": score[5],
        "r7": score[6],
        "r8": score[7],
        "r9": score[8],
        "r10": score[9],
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
        required_fields = ["r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8", "r9", "r10", "categoria", "nombre", "apellido", "telefono", "correo"]
        missing_fields = [field for field in required_fields if field not in data]

        if missing_fields:
            return jsonify({"error": "Faltan campos obligatorios", "missing_fields": missing_fields}), 400

        # Extraer valores
        r1, r2, r3, r4, r5, r6, r7, r8, r9, r10 = data["r1"], data["r2"], data["r3"], data["r4"], data["r5"], data["r6"], data["r7"], data["r8"], data["r9"], data["r10"]
        trabajo, nombre, apellido, telefono, correo = data["categoria"], data["nombre"], data["apellido"], data["telefono"], data["correo"]

        # Generar PDF
        url = createPDF.generate_pdf(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, trabajo, nombre, apellido, telefono, correo)

        # Respuesta
        return jsonify({"url_pdf": url}), 201

    except Exception as e:
        return jsonify({"error": "Ocurrió un error en el servidor", "detalle": str(e)}), 500
    
# Endpoint POST
@app.route('/match', methods=['POST'])
def match_users():
    pass

if __name__ == '__main__':
    app.run(debug=True)
