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
        jwt = responseData.jwt;
        console.log(jwt)
    } catch (error) {
        console.error("Error en la solicitud:", error);
    }
    
});


