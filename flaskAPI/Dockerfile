FROM python:3.10-slim

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Instalamos dependencias del sistema necesarias para wkhtmltopdf
RUN apt-get update && apt-get install -y \
    wget \
    xfonts-75dpi \
    xfonts-base \
    && rm -rf /var/lib/apt/lists/*

# Descargamos e instalamos wkhtmltopdf
RUN wget https://github.com/wkhtmltopdf/packaging/releases/download/0.12.6-1/wkhtmltox_0.12.6-1.buster_amd64.deb \
    && dpkg -i wkhtmltox_0.12.6-1.buster_amd64.deb || true \
    && apt-get install -f -y \
    && rm wkhtmltox_0.12.6-1.buster_amd64.deb

# Copiamos todos los archivos dentro del contenedor
COPY . /app

# Instalamos las dependencias de Python
RUN pip install --no-cache-dir -r requirements.txt

# Exponemos el puerto 5000
EXPOSE 5000

# Ejecutamos la aplicación
CMD ["python", "app.py"]
