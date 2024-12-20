const inputDate = document.getElementById('fecha');
const inputTime = document.getElementById('hora');
const placaInput = document.getElementById('placa');
const placaText = document.getElementById('placa-text');
const tipoSelect = document.getElementById('tipo');
const form = document.getElementById('vehiculo-form');

const popup = document.getElementById('popup');
const title_message = document.getElementById('title-message-popup');
const message_popup = document.getElementById('message-popup');

document.addEventListener('DOMContentLoaded', (event) => {
    // obtener fecha y hora actuales
    let today = new Date();

    // obtener la fecha actual en formato YYYY-MM-DD
    let date = today.toISOString().split('T')[0];

    // función para actualizar la hora
    function updateTime() {
        let now = new Date();
        let hours = String(now.getHours()).padStart(2, '0');
        let minutes = String(now.getMinutes()).padStart(2, '0');
        let seconds = String(now.getSeconds()).padStart(2, '0');
        let time = `${hours}:${minutes}:${seconds}`;
        inputTime.value = time;

        date = today.toISOString().split('T')[0];
        inputDate.value = date;
    }

    // actualizar la hora inmediatamente
    updateTime();

    // actualizar la hora cada segundo
    setInterval(updateTime, 1000);

    // ----------------------------form-------------------------------

    // Evento para validar la placa a medida que se escribe
    placaInput.addEventListener('input', (event) => {
        // Eliminar caracteres no permitidos y agregar espacio
        let value = placaInput.value.replace(/[^A-Z0-9]/gi, '').toUpperCase();
        if (value.length > 3) {
            value = value.slice(0, 3) + '  ' + value.slice(3);
        }

        if (validatePlaca()) {
            // Actualizar el texto de la placa
            placaText.textContent = value;
        }
    });

    const closeBtn = document.getElementById('closeBtn');
    const backgroundPopup = document.getElementById('popup');

    // Cerrar el pop-up cuando el usuario haga clic en la 'X'
    closeBtn.addEventListener('click', function () {
        document.getElementById('popup').style.display = 'none';
    });

    backgroundPopup.addEventListener('click', function () {
        document.getElementById('popup').style.display = 'none';
    });

    // Habilitar el campo de entrada de la placa al seleccionar un tipo de vehículo
    tipoSelect.addEventListener('change', (event) => {
        placaInput.value = '';
        placaText.textContent = '--- ---';
        if (tipoSelect.value) {
            placaInput.disabled = false;
        } else {
            placaInput.disabled = true;
        }
    });
});

export function insertMessagePopUp(title, message) {
    title_message.textContent = title;

    if (typeof message === 'string') {
        title_message.textContent = title + ': ' + message;
        message_popup.textContent = '';
        return;
    }

    let vehicleData = {
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
    }

    let vehicleDataHTML = '';
    for (let key in vehicleData) {
        vehicleDataHTML += `<p><strong>${key}:</strong> ${vehicleData[key]}</p>`;
    }
    message_popup.innerHTML = vehicleDataHTML;
}

// Validar el formato de la placa
export function validatePlaca() {
    const tipo = tipoSelect.value;
    const placa = placaInput.value.toUpperCase();
    let isValid = false;

    if (tipo === 'car') {
        if (placa.length <= 3) {
            isValid = /^[A-Z]{0,3}$/.test(placa);
        } else {
            isValid = /^[A-Z]{3}[0-9]{0,3}$/.test(placa);
        }
    } else if (tipo === 'motorcycle') {
        if (placa.length <= 3) {
            isValid = /^[A-Z]{0,3}$/.test(placa);
        } else if (placa.length <= 5) {
            isValid = /^[A-Z]{3}[0-9]{1,2}$/.test(placa);
        } else {
            isValid = /^[A-Z]{3}[0-9]{2}[A-Z]$/.test(placa);
        }
    }
    if (!isValid) {
        // eliminar el caracter escrito 
        placaInput.value = placaInput.value.slice(0, -1);
    }

    return isValid;
}