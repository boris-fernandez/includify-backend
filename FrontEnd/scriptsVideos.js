// Definir los elementos globales correctamente
const containerAnuncios = document.querySelector(".container_anuncios");
const containerVideos = document.querySelector(".container_videos");
const player = document.querySelector(".player");

// Función para alternar la visibilidad del contenedor de anuncios
document.querySelector(".btnExit").addEventListener("click", function() {
    if (window.innerWidth > 1200) {
        const icon = this.querySelector("i");

        // Alternar clases del icono
        icon.classList.toggle("ti-logout");
        icon.classList.toggle("ti-logout-2");

        // Obtener el estado actual del contenedor de anuncios
        const isHidden = window.getComputedStyle(containerAnuncios).display === "none";

        if (isHidden) {
            containerAnuncios.style.display = "flex";
            containerVideos.style.width = "40vw";
        } else {
            containerAnuncios.style.display = "none";
            containerVideos.style.width = "85vw";
        }
    }
});




/*###################################################*/

// Configuración del Intersection Observer
const observer = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
      const video = entry.target;
      if (entry.isIntersecting) {
          video.play(); // Reproducir cuando el video esté visible
      } else {
          video.currentTime = 0;
          video.pause(); // Pausar cuando no está visible
      }
  });
}, {
  threshold: 0.5
});

// Observar todos los videos
const videos = document.querySelectorAll('.video_player');
videos.forEach(video => {
  observer.observe(video);
});

const videos_inclusivos = document.querySelectorAll('.video_player_inclusivo');
videos_inclusivos.forEach(video_inclusivo => {
  observer.observe(video_inclusivo);
});

videos.forEach(video => {
  video.addEventListener('click', () => {
      // Buscar el video inclusivo dentro del mismo .player
      const container = video.closest('.player'); // Encuentra el contenedor principal
      const video_inclusivo = container?.querySelector('.video_player_inclusivo'); // Busca el video inclusivo

      if (video.paused) {
          video.muted = false;
          video.play();  // Reproducimos el video principal
          if (video_inclusivo) video_inclusivo.play(); // Reproducimos el inclusivo si existe
      } else {
          video.muted = false;
          video.pause();  // Pausamos el video principal
          if (video_inclusivo) video_inclusivo.pause(); // Pausamos el inclusivo si existe
      }
  });
});


/* DESPLAZAMIENTO CON FLECHAS */

// Seleccionamos el contenedor que tiene los videos
const videosContainer = document.querySelector(".videos");

// Seleccionamos todos los videos
const players = document.querySelectorAll(".player");

// Seleccionamos las flechas de navegación
const arrowUp = document.querySelector(".arrow:first-child");
const arrowDown = document.querySelector(".arrow-Down");

// Índice del video actual
let currentIndex = 0;

// Función para desplazar al siguiente video
const scrollToVideo = (index) => {
    if (index < 0 || index >= players.length) return; // Evitar salir de rango
    currentIndex = index;

    // Obtener la posición del video seleccionado
    const targetVideo = players[currentIndex];
    videosContainer.scrollTo({
        top: targetVideo.offsetTop - videosContainer.offsetTop,
        behavior: "smooth"
    });
};

// Función para actualizar el índice según el scroll
const updateCurrentIndexOnScroll = () => {
    let minDiff = Infinity;
    let newIndex = currentIndex;

    players.forEach((player, index) => {
        const rect = player.getBoundingClientRect();
        const diff = Math.abs(rect.top - videosContainer.getBoundingClientRect().top);

        if (diff < minDiff) {
            minDiff = diff;
            newIndex = index;
        }
    });

    currentIndex = newIndex;
};

// Event Listeners para las flechas
arrowUp.addEventListener("click", (e) => {
    e.preventDefault();
    scrollToVideo(currentIndex - 1);
});

arrowDown.addEventListener("click", (e) => {
    e.preventDefault();
    scrollToVideo(currentIndex + 1);
});

// Detectar scroll manual y actualizar el índice
videosContainer.addEventListener("scroll", () => {
    updateCurrentIndexOnScroll();
});
