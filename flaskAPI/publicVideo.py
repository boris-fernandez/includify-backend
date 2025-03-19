import os
import cloudinary
import cloudinary.uploader

# Configuration       
cloudinary.config( 
    cloud_name = os.getenv("CLOUDINARY_CLOUD_NAME"), 
    api_key = os.getenv("CLOUDINARY_API_KEY"),
    api_secret = os.getenv("CLOUDINARY_API_SECRET"),
    secure=True
)

def upload_video(typeVideo):
    if typeVideo == "Tiktok":
        foldervideo =  "video_final.mp4"
    elif typeVideo == "Señas":
        foldervideo = "Señas.mp4"
    else:
        return "Error: Tipo de video no valido", 400

    upload_response = cloudinary.uploader.upload_large(
       foldervideo, 
        resource_type="video"
    )

    # Obtener la URL del video subido
    video_url = upload_response.get("secure_url")
    return(video_url)