import { insertMessagePopUp } from './main.js';

const form = document.getElementById('vehiculo-form');
const popup = document.getElementById('popup');
const message_error = document.getElementById('message-error');


function organizeVehicleData(message) {
    return {
        'PLACA': message.license_plate.toUpperCase(),
        'TIPO': message.vehicle_type == 'car' ? 'CARRO' : 'MOTO',
        'FECHA Y HORA DE ENTRADA': new Date(message.entry_date).toLocaleString('es-ES', {
            day: 'numeric',
            month: 'long',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            hour12: true
        }),
        'FECHA Y HORA DE PAGO': new Date(message.payment_date).toLocaleString('es-ES', {
            day: 'numeric',
            month: 'long',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            hour12: true
        }),
    }
}

// Validar la placa al enviar el formulario
form.addEventListener('submit', (event) => {
    event.preventDefault(); // Evita el envío del formulario

    const vehicleData = {
        license_plate: event.target.placa.value.toUpperCase()
    };

    // Hacer la solicitud POST usando fetch
    fetch("http://localhost:5000/api/v1/exits", {
        method: "POST",
        headers: {
            "Content-Type": "application/json", // Indicamos que enviamos JSON
        },
        body: JSON.stringify(vehicleData) // Convertir el objeto a JSON
    })
        .then(response => response.json())
        .then(data => {
            console.log("Respuesta del servidor:", data);
            if (!data.message) {
                popup.style.display = 'block';
                insertMessagePopUp('Salida registrada con exito', organizeVehicleData(data));
            } else {
                message_error.style.display = 'block';
                message_error.textContent = data.message;
            }
            // reiniciar el formulario
            form.reset();
        })
        .catch(error => {
            console.error("Error:", error);
        });
});