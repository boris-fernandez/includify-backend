FROM python:3.10-slim

WORKDIR /flaskAPI

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
        ffmpeg && \
    rm -rf /var/lib/apt/lists/*

COPY requirements.txt ./

RUN python -m pip install --upgrade pip 

RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 5000

CMD ["gunicorn", "-b", "0.0.0.0:5000", "--timeout", "600", "--graceful-timeout", "600", "app:app"]
