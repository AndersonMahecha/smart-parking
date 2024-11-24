import { insertMessagePopUp } from './main.js';
const form = document.getElementById('vehiculo-form');
const popup = document.getElementById('popup');
const button_popup = document.getElementById('button-popup');
const checkmark = document.getElementById('checkmark');
const message_error = document.getElementById('message-error');

function organizeVehicleData(vehicle) {
    return {
        'PLACA': vehicle.license_plate.toUpperCase(),
        'TIPO': vehicle.vehicle_type == 'car' ? 'CARRO' : 'MOTO',
        'TIEMPO DE ENTRADA': new Date(vehicle.entry_date).toLocaleString('es-ES', {
            day: 'numeric',
            month: 'long',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            hour12: true
        }),
        'TIEMPO ACTUAL': new Date(vehicle.current_time).toLocaleString('es-ES', {
            day: 'numeric',
            month: 'long',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            hour12: true
        }),
        'TIEMPO DE PARQUEO': convertMinutesToHours(vehicle.total_minutes),
        'VALOR POR MINUTO': '$' + vehicle.cost_per_minute.toLocaleString('es-ES'),
        'VALOR A PAGAR': '$' + Math.round(vehicle.total_cost).toLocaleString('es-ES'),
        'UBICACIÓN': vehicle.parking_slot_id,
    }
}
function organizeVehiclePay(vehicle) {
    return {
        'PLACA': vehicle.license_plate.toUpperCase(),
        'TIPO': vehicle.vehicle_type == 'car' ? 'CARRO' : 'MOTO',
        'FECHA DE PAGO': new Date(vehicle.payment_date).toLocaleString('es-ES', {
            day: 'numeric',
            month: 'long',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            hour12: true
        }),
    }
}

function convertMinutesToHours(minutes) {
    const totalMinutes = Math.floor(minutes);
    const hours = Math.floor(totalMinutes / 60);
    const remainingMinutes = totalMinutes % 60;
    return `${hours} horas - ${remainingMinutes} minutos`;
}

let dataVehicle = {};
// Evento para manejar el envío del formulario
form.addEventListener('submit', (event) => {
    event.preventDefault(); // Prevenir el envío normal del formulario

    const placa = event.target.placa.value.toUpperCase(); // Obtener la placa en mayúsculas
    const params = { license_plate: placa }; // Crear los parámetros de URL

    // Hacer la solicitud POST usando fetch
    fetch("http://localhost:5000/api/v1/parking/pay/info", {
        method: "POST",
        headers: {
            "Content-Type": "application/json", // Indicamos que enviamos JSON
        },
        body: JSON.stringify(params) // Convertir los parámetros a una cadena
    })
        .then(response => response.json())
        .then(data => {
            console.log("Respuesta del servidor:", data);
            if (!data.message) {
                popup.style.display = 'block';
                insertMessagePopUp('Información de pago', organizeVehicleData(data), 'Pagar');
                dataVehicle = data;
            } else {
                message_error.style.display = 'block';
                message_error.textContent = data.message;
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });
    // Reiniciar el formulario después de mostrar la respuesta
    form.disabled = true;
    form.reset();
});

button_popup.addEventListener('click', () => {
    const params = { vehicle_id: dataVehicle.identifier };
    // Hacer la solicitud POST usando fetch
    fetch("http://localhost:5000/api/v1/parking/pay", {
        method: "POST",
        headers: {
            "Content-Type": "application/json", // Indicamos que enviamos JSON
        },
        body: JSON.stringify(params) // Convertir los parámetros a una cadena
    })
        .then(response => response.json())
        .then(data => {
            console.log("Respuesta del servidor:", data);
            if (!data.message) {
                if (data.payment_date) {
                    popup.style.display = 'block';
                    checkmark.style.display = 'block';
                    insertMessagePopUp('Pago exitoso', organizeVehiclePay(data));
                }
            } else {
                popup.style.display = 'block';
                message_error.style.display = 'block';
                message_error.textContent = data.message;
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });
});