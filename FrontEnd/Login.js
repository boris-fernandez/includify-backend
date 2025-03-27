var URL = 'http://localhost:8080/'
let jwt = null;

const dataForm = document.querySelector('.loginForm');

dataForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const datosFormulario = new FormData(dataForm);
    const data = {
        correo: datosFormulario.get('correo'),
        contrasena: datosFormulario.get('contrasena')
    };

    try {
        const response = await fetch(URL + "auth/login", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        const responseData = await response.json();
        if (response.ok) {
            localStorage.setItem('jwt', responseData.jwt);
            console.log("JWT almacenado:", responseData.jwt);
        } else {
            console.error("Error en la autenticación:", responseData);
        }
    } catch (error) {
        console.error("Error en la solicitud:", error);
    }
    
});


document.getElementById("btnCrearCuenta").addEventListener("click", function() {
    document.getElementById("modal").style.display = "flex";
});

document.getElementById("btnCerrar").addEventListener("click", function() {
    document.getElementById("modal").style.display = "none";
});