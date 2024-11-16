import { insertMessagePopUp } from './main.js';

const form = document.getElementById('vehiculo-form');
const popup = document.getElementById('popup');


// Validar la placa al enviar el formulario
form.addEventListener('submit', (event) => {
    event.preventDefault(); // Evita el envío del formulario

    const vehicleData = {
        license_plate: event.target.placa.value.toUpperCase(),
        vehicle_type: event.target.tipo.value,
        // entry_date: colombiaTime,  // Fecha en formato "YYYY-MM-DD HH:MM:SS"
    };

    // Hacer la solicitud POST usando fetch
    fetch("http://localhost:5000/api/v1/entries", {
        method: "POST",
        headers: {
            "Content-Type": "application/json", // Indicamos que enviamos JSON
        },
        body: JSON.stringify(vehicleData) // Convertir el objeto a JSON
    })
        .then(response => response.json())
        .then(data => {
            console.log("Respuesta del servidor:", data);
            popup.style.display = 'block';
            if (!data.message) {
                insertMessagePopUp('VehÍculo registrado con exito', data);
            } else {
                insertMessagePopUp('Error', data.message);
            }
            // reiniciar el formulario
            form.reset();
        })
        .catch(error => {
            console.error("Error:", error);
        });
});