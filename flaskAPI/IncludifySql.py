from mysql.connector import pooling
import os

# Crear un pool de conexiones con 5 conexiones disponibles
dbconfig = {
    "user": os.getenv('MYSQL_USER'),
    "host": os.getenv('MYSQL_HOST'),
    "password": os.getenv('MYSQL_PASSWORD'),
    "database": os.getenv('MYSQL_DATABASE'),
    "port": int(os.getenv('MYSQL_PORT')),
}

pool = pooling.MySQLConnectionPool(pool_name="mypool",
                                   pool_size=1,
                                   **dbconfig)

def get_connection():
    """Obtener una conexión del pool."""
    return pool.get_connection()

def exSQL(consulta):
    """Ejecuta una consulta SQL y devuelve los resultados."""
    conexion = get_connection()
    miCursor = conexion.cursor()

    miCursor.execute(consulta)
    response = miCursor.fetchall()

    miCursor.close()
    conexion.close() 
    return response

# Ejemplo de uso
if __name__ == "__main__":
    query = "SELECT * FROM candidatos LIMIT 10;"
    resultados = exSQL(query)
    for row in resultados:
        print(row)
