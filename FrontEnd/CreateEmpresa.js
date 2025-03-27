var URL = 'http://localhost:8080/'

const formulario = document.querySelector('.formulario');
 
 formulario.addEventListener('submit', event => {
     event.preventDefault();
 
     const datosFormulario = new FormData(formulario);
     const data = {
         usuario: {
             correo: datosFormulario.get('email'),
             contrasena: datosFormulario.get('password')
         },
         nombre: datosFormulario.get('empresa'),
         telefono: datosFormulario.get('telefono'),
         pais: datosFormulario.get('pais'),
         telefono: datosFormulario.get('telefono')
     };
 
     console.log(data)
     fetch(URL + "auth/registrar/empresa", {
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