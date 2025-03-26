var URL = 'http://localhost:8080/'
let jwt = null;

const dataForm = document.querySelector('.loginForm');
const formulario = document.querySelector('.formulario');

formulario.addEventListener('submit', event => {
    event.preventDefault();

    const datosFormulario = new FormData(formulario);
    const data = {
        usuario: {
            correo: datosFormulario.get('email'),
            contrasena: datosFormulario.get('password')
        },
        nombre: datosFormulario.get('nombre'),
        apellidos: datosFormulario.get('apellidos'),
        telefono: datosFormulario.get('telefono'),
        respuestas: [1, 0, 3, 1, 3, 0, 2, 1, 1, 3],
        categoria: "Inteligencia artificial" 
    };

    fetch(URL + "auth/registrar/candidato", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (response.status === 201) {
            console.log("Registro exitoso");
            return; 
        }
        return response.json();
    })
    .then(data => {
        if (data) console.log("Error en la respuesta:", data);
    })
    .catch(error => console.log('Error en la solicitud:', error));
    
});

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

    } catch (error) {
        console.error("Error en la solicitud:", error);
    }
    
});

