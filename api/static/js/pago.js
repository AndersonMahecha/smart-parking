// Selección de los elementos
const form = document.getElementById('vehiculo-form');
const popup = document.getElementById('popup');
const popupMessage = document.getElementById('popup-message');

// Función para obtener la hora actual
function getCurrentTime() {
    const now = new Date();
    return now.toLocaleTimeString(); // Devuelve la hora en formato HH:MM:SS AM/PM
}

// Función que simula el "quemado" de datos
function simulateData(licensePlate) {
    // Datos simulados para la placa ingresada
    const simulatedData = {
        license_plate: licensePlate,
        entry_date: "2024-11-21", // Fecha de ingreso simulada
        entry_time: "10:30 AM",   // Hora de ingreso simulada
        current_date: new Date().toLocaleDateString(), // Fecha actual
        current_time: getCurrentTime(), // Hora actual
        payment_per_minute: 70, // Valor por hora simulado
        total_payment: 70 * 120   // Pago total simulado por 2 horas
    };

    return simulatedData;
}

// Evento para manejar el envío del formulario
form.addEventListener('submit', (event) => {
    event.preventDefault(); // Prevenir el envío normal del formulario

    const placa = event.target.placa.value.toUpperCase(); // Obtener la placa en mayúsculas
    const params = new URLSearchParams({ license_plate: placa }); // Crear los parámetros de URL

    // Hacer la solicitud POST usando fetch
    fetch("http://localhost:5000/api/v1/paid", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded", // Indicamos que enviamos URL-encoded
        },
        body: params.toString() // Convertir los parámetros a una cadena
    })
        .then(response => response.json())
        .then(data => {
            console.log("Respuesta del servidor:", data);
            popup.style.display = 'block';
            // if (!data.message) {
            //     insertMessagePopUp('Vehículo registrado con éxito', data);
            // } else {
            //     insertMessagePopUp('Error', data.message);
            // }
            // reiniciar el formulario
            form.reset();
        })
        .catch(error => {
            console.error("Error:", error);
        });
    // Simular la respuesta de datos
    const data = simulateData(placa);

    // Mostrar el popup con la información de pago
    popup.style.display = 'block';
    const message = `
        <strong>Placa digitada:</strong> ${data.license_plate}<br>
        <strong>Fecha de ingreso:</strong> ${data.entry_date}<br>
        <strong>Hora de ingreso:</strong> ${data.entry_time}<br>
        <strong>Fecha actual:</strong> ${data.current_date}<br>
        <strong>Hora actual:</strong> ${data.current_time}<br>
        <strong>Valor hora:</strong> $${data.payment_per_minute}<br>
        <strong>Valor a pagar:</strong> $${data.total_payment}
    `;
    popupMessage.innerHTML = message;

    // Reiniciar el formulario después de mostrar la respuesta
    form.reset();
});