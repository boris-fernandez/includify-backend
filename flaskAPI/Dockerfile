FROM python:3.10-bookworm

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
        && rm -rf /var/lib/apt/lists/*

# Descargar e instalar wkhtmltopdf
RUN wget -O wkhtmltox.deb https://github.com/wkhtmltopdf/packaging/releases/download/0.12.6-1/wkhtmltox_0.12.6-1.bookworm_amd64.deb && \
    dpkg -i wkhtmltox.deb || apt-get install -f -y && \
    rm wkhtmltox.deb

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
CMD ["python", "main.py"]
