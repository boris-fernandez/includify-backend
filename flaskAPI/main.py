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
        print("1")
        # Intentar obtener JSON
        data = request.get_json()
        print("2")

        if not data:
            return jsonify({"error": "El cuerpo de la solicitud está vacío o no es un JSON válido"}), 400
        print("3")
        # Validar que todos los campos estén presentes
        required_fields = ["respuestas", "categoria", "nombre", "apellido", "telefono", "correo"]
        missing_fields = [field for field in required_fields if field not in data]
        print("4")
        if missing_fields:
            return jsonify({"error": "Faltan campos obligatorios", "missing_fields": missing_fields}), 400
        print("5")
        # Validar que "respuestas" sea una lista con 10 elementos
        if not isinstance(data["respuestas"], list):
            return jsonify({"error": "El campo 'respuestas' debe ser una lista"}), 400
        print("6")
        if len(data["respuestas"]) != 10:
            return jsonify({"error": "La lista 'respuestas' debe contener exactamente 10 elementos"}), 400
        print("7")
        # Extraer valores
        try:
            r1, r2, r3, r4, r5, r6, r7, r8, r9, r10 = data["respuestas"]
        except ValueError:
            return jsonify({"error": "La lista 'respuestas' contiene un número incorrecto de elementos"}), 400
        print("8")
        trabajo = data.get("categoria")
        nombre = data.get("nombre")
        apellido = data.get("apellido")
        telefono = data.get("telefono")
        correo = data.get("correo")

        # Validar que los campos sean cadenas no vacías
        for field, value in {"categoria": trabajo, "nombre": nombre, "apellido": apellido, "telefono": telefono, "correo": correo}.items():
            if not isinstance(value, str) or not value.strip():
                return jsonify({"error": f"El campo '{field}' debe ser una cadena no vacía"}), 400

        # Generar PDF
        try:
            url = createPDF.generate_pdf(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, trabajo, nombre, apellido, telefono, correo)
        except Exception as e:
            return jsonify({"error": "Error al generar el PDF", "detalle": str(e)}), 500

        return jsonify({"pdf_url": url}), 201

    except Exception as e:
        return jsonify({"error": "Error interno del servidor", "detalle": str(e)}), 500

    
# Endpoint POST
@app.route('/match', methods=['POST'])
def match_users():
    
    pass

if __name__ == '__main__':
    app.run(debug=True)
