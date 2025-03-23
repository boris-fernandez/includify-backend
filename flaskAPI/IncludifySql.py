import mysql.connector

# Función para obtener una conexión nueva
def get_connection():
    return mysql.connector.connect(
        user='ab66cd_includi',
        host='MYSQL1001.site4now.net',
        password='Includify@2025!',
        database='db_ab66cd_includi',
        port=3306
    )

def exSQL(consulta):
    conexion = get_connection()  # Crear una nueva conexión en cada consulta
    miCursor = conexion.cursor()

    miCursor.execute(consulta)
    response = miCursor.fetchall()

    miCursor.close()
    conexion.close()
    return response
