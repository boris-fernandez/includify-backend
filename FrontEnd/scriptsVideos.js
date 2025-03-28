document.querySelector(".btnExit").addEventListener("click", function() {
  if (window.innerWidth > 1200) { // Verifica el ancho de la pantalla
      const icon = this.querySelector("i");
      const containerAnuncios = document.querySelector(".container_anuncios");
      const containerVideos = document.querySelector(".container_videos");

      // Alternar clases del icono
      icon.classList.toggle("ti-logout");
      icon.classList.toggle("ti-logout-2");

      // Obtener el estilo computado del contenedor de anuncios
      const computedStyle = window.getComputedStyle(containerAnuncios);
      const isHidden = computedStyle.display === "none";

      // Alternar visibilidad del contenedor de anuncios y ajustar el ancho de container_videos
      if (isHidden) {
          containerAnuncios.style.display = "flex"; // Mostrar el contenedor
          containerVideos.style.width = "40vw"; // Restaurar tamaño original
      } else {
          containerAnuncios.style.display = "none"; // Ocultar el contenedor
          containerVideos.style.width = "85vw"; // Ampliar el contenedor de videos
      }
  }
});


const player = document.querySelector(".player");

// Función para verificar el ancho de .player
const checkPlayerWidth = () => {
    if (window.getComputedStyle(player).width === "100vw") {
        containerAnuncios.style.display = "none";
    } else {
        containerAnuncios.style.display = "flex";
    }
};

// Configurar MutationObserver para detectar cambios en los estilos
const observer = new MutationObserver(checkPlayerWidth);
observer.observe(player, { attributes: true, attributeFilter: ["style"] });

// Ejecutar la función al cargar la página
checkPlayerWidth();

