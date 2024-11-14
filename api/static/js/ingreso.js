document.addEventListener('DOMContentLoaded', (event) => {
    const inputDate = document.getElementById('fecha');
    const inputTime = document.getElementById('hora');
    const placaInput = document.getElementById('placa');
    const placaText = document.getElementById('placa-text');
    const tipoSelect = document.getElementById('tipo');
    const form = document.getElementById('vehiculo-form');
    // obtener fecha y hora actuales
    let today = new Date();

    // obtener la fecha actual en formato YYYY-MM-DD
    let date = today.toISOString().split('T')[0];

    inputDate.value = date;

    // función para actualizar la hora
    function updateTime() {
        let now = new Date();
        let hours = String(now.getHours()).padStart(2, '0');
        let minutes = String(now.getMinutes()).padStart(2, '0');
        let seconds = String(now.getSeconds()).padStart(2, '0');
        let time = `${hours}:${minutes}:${seconds}`;
        inputTime.value = time;
    }

    // actualizar la hora inmediatamente
    updateTime();

    // actualizar la hora cada segundo
    setInterval(updateTime, 1000);

    // ----------------------------form-------------------------------

    // Validar el formato de la placa
    function validatePlaca() {
        const tipo = tipoSelect.value;
        const placa = placaInput.value.toUpperCase();
        let isValid = false;

        if (tipo === 'carro') {
            if (placa.length <= 3) {
                isValid = /^[A-Z]{0,3}$/.test(placa);
            } else {
                isValid = /^[A-Z]{3}[0-9]{0,3}$/.test(placa);
            }
        } else if (tipo === 'moto') {
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

    // Validar la placa al enviar el formulario
    form.addEventListener('submit', (event) => {
        if (!validatePlaca()) {
            event.preventDefault(); // Evita el envío del formulario
            alert('El formato de la placa es incorrecto.');
        }
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

        // if (tipoSelect.value === 'carro') {
        //     placaInput.pattern = "^[A-Z]{3}[0-9]{3}$";
        //     placaInput.title = "Formato de placa para carro: AAA111";
        // }
        // if (tipoSelect.value === 'moto') {
        //     placaInput.pattern = "^[A-Z]{3}[0-9]{2}[A-Z]$";
        //     placaInput.title = "Formato de placa para moto: AAA12B";
        // }
    });
});