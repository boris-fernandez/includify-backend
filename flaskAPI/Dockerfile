FROM python:3.10-slim

# Configurar el directorio de trabajo
WORKDIR /flaskAPI

# Instalar dependencias del sistema
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        wget \
        xfonts-75dpi \
        xfonts-base \
        libxrender1 \
        libxext6 \
        libfontconfig1 \
        libssl3 \
        libjpeg62-turbo \
        libpng16-16 \
        && rm -rf /var/lib/apt/lists/*

# Copiar archivo de dependencias de Python
COPY requirements.txt ./

# Instalar la última versión de pip
RUN python -m pip install --upgrade pip 

# Instalar paquetes de Python
RUN pip install --no-cache-dir -r requirements.txt

# Copiar el código de la aplicación
COPY . .

# Exponer el puerto 5000
EXPOSE 5000

# Ejecutar la aplicación
CMD ["gunicorn", "-b", "0.0.0.0:5000", "app:app"]