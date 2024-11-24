const inputDate = document.getElementById('fecha');
const inputTime = document.getElementById('hora');
const placaInput = document.getElementById('placa');
const placaText = document.getElementById('placa-text');
const tipoSelect = document.getElementById('tipo');

const form = document.getElementById('vehiculo-form');
const title_message = document.getElementById('title-message-popup');
const message_popup = document.getElementById('message-popup');
const button_popup = document.getElementById('button-popup');

const message_error = document.getElementById('message-error');

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

    const closeBtn = document.getElementById('closeBtn');
    const backgroundPopup = document.getElementById('popup');

    function closePopUp() {
        document.getElementById('popup').style.display = 'none';
        form.disabled = false;
    }

    // Cerrar el pop-up cuando el usuario haga clic en la 'X'
    closeBtn.addEventListener('click', closePopUp);
    backgroundPopup.addEventListener('click', closePopUp);

    // Evento para validar la placa a medida que se escribe
    placaInput.addEventListener('input', (event) => {
        message_error.style.display = 'none';

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

    // Habilitar el campo de entrada de la placa al seleccionar un tipo de vehículo
    if (tipoSelect) {
        placaInput.disabled = true;
        tipoSelect.addEventListener('change', (event) => {
            placaInput.value = '';
            placaText.textContent = '--- ---';
            if (tipoSelect.value) {
                placaInput.disabled = false;
            } else {
                placaInput.disabled = true;
            }
        });
    } else {
        placaInput.disabled = false;
    }
});

export function insertMessagePopUp(title, vehicle, buttonTxt = 'Default') {
    title_message.textContent = title;

    if (typeof vehicle === 'string') {
        title_message.textContent = title + ': ' + vehicle;
        message_popup.textContent = '';
        return;
    }

    let vehicleData = vehicle;

    // Limpiar el contenido anterior de la tabla
    message_popup.innerHTML = '';

    // Insertar las filas de datos en la tabla
    for (let key in vehicleData) {
        let row = document.createElement('tr');
        let cellKey = document.createElement('td');
        let cellValue = document.createElement('td');
        cellKey.textContent = key;
        cellValue.textContent = vehicleData[key];
        row.appendChild(cellKey);
        row.appendChild(cellValue);
        message_popup.appendChild(row);
    }
    if (button_popup) {
        if (buttonTxt != 'Default') {
            button_popup.textContent = buttonTxt;
        } else {
            button_popup.style.display = 'none';
        }
    }
}

// Validar el formato de la placa
export function validatePlaca() {
    let isValid = false;
    const placa = placaInput.value.toUpperCase();

    if (tipoSelect) {
        const tipo = tipoSelect.value;

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
    } else {
        if (placa.length <= 3) {
            isValid = /^[A-Z]{0,3}$/.test(placa);
        } else if (placa.length <= 5) {
            isValid = /^[A-Z]{3}[0-9]{1,2}$/.test(placa);
        } else {
            isValid = /^[A-Z]{3}[0-9]{2}[A-Z0-9]$/.test(placa);
        }
    }
    if (!isValid) {
        // eliminar el caracter escrito 
        placaInput.value = placaInput.value.slice(0, -1);
    }

    return isValid;
}