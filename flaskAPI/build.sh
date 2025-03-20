#!/bin/bash
# set -e  # Detiene la ejecución si hay algún error

# Instalar dependencias del sistema
# apt-get update
apt-get install -y ffmpeg

# Descargar e instalar wkhtmltopdf
wget https://github.com/wkhtmltopdf/packaging/releases/download/0.12.6-1/wkhtmltox_0.12.6-1.buster_amd64.deb
dpkg -i wkhtmltox_0.12.6-1.buster_amd64.deb || true
apt-get install -f -y

# Verificar que FFmpeg se instaló correctamente
ffmpeg -version

# Instalar dependencias de Python
pip install -r requirements.txt


wget https://github.com/wkhtmltopdf/packaging/releases/download/0.12.6-1/wkhtmltox_0.12.6-1.buster_amd64.deb
dpkg -i wkhtmltox_0.12.6-1.buster_amd64.deb || true
apt-get install -f -y
pip install -r requirements.txt