import traceback
import time
from flask import Flask, jsonify, request
import videoTiktok
import videoSeñas
import califications
import publicVideo
import createPDF
import categoriasTrabajo
import IncludifySql
import WorkClustering

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

    return respuesta

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
            "pdf_url": url[0],
        })

        return respuesta

    except Exception as e:
        print(traceback.format_exc())  # 🔍 Esto imprimirá el error completo en la consola de Render
        return jsonify({"error": "Ocurrió un error en el servidor", "detalle": str(e)}), 500

    
# Endpoint POST
@app.route('/match', methods=['POST'])
def match_users():
    data = request.get_json()
    primary_key = data.get("pk_usuario")  # Usar .get() para evitar errores si no existe
    
    # Proteger contra inyección SQL usando parámetros seguros
    respuesta = IncludifySql.exSQL(f"SELECT candidatos.id_usuario, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, respuestas_candidato.id_categoria FROM respuestas_candidato INNER JOIN candidatos ON respuestas_candidato.id_candidato = candidatos.id WHERE candidatos.id_usuario = {primary_key}")
    usuario = [] # <- Lista que guarda los datos de las respustas del candidato
    for fila in respuesta:
        for i in range(12):
            usuario.append(fila[i])

    re1 = usuario[1]
    categoria = usuario[11]
    del usuario[1]
    del usuario[10]

    todos = IncludifySql.exSQL(f"select empleos.id,r2,r3,r4,r5,r6,r7,r8,r9,r10 from respuestas_empleo inner join empleos on id_empleo=empleos.id where id_categoria={categoria};")

    data_lists = [list(t) for t in todos]
    # print(data_lists)

    pks = ["usuario"]
    r2 = [usuario[1]]
    r3 = [usuario[2]]
    r4 = [usuario[3]]
    r5 = [usuario[4]]
    r6 = [usuario[5]]
    r7 = [usuario[6]]
    r8 = [usuario[7]]
    r9 = [usuario[8]]
    r10 = [usuario[9]]

    for i in range(len(data_lists)):
        pks.append(data_lists[i][0])
        r2.append(data_lists[i][1])
        r3.append(data_lists[i][2])
        r4.append(data_lists[i][3])
        r5.append(data_lists[i][4])
        r6.append(data_lists[i][5])
        r7.append(data_lists[i][6])
        r8.append(data_lists[i][7])
        r9.append(data_lists[i][8])
        r10.append(data_lists[i][9])

    datos = pd.DataFrame({"r2" : r2,
                             "r3" : r3,
                             "r4" : r4,
                             "r5" : r5,
                             "r6" : r6,
                             "r7" : r7,
                             "r8" : r8,
                             "r9" : r9,
                             "r10" : r10})
    
    columns=["r2", "r3", "r4", "r5", "r6", "r7", "r8", "r9", "r10"]

    pks = list(map(str, pks))

    recomendacion = WorkClustering.procesarClustering(pks, datos, columns)
    print(recomendacion)
    
    return recomendacion, 200

if __name__ == '__main__':
    app.run(debug=True)
