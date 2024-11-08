document.addEventListener('DOMContentLoaded', (event) => {
    // obtener fecha y hora actuales
    let today = new Date();

    // obtener la fecha actual en formato YYYY-MM-DD
    let date = today.toISOString().split('T')[0];

    // obtener la hora actual en formato HH:MM
    let time = today.toTimeString().split(' ')[0].slice(0, 5);

    let inputDate = document.getElementById('fecha');
    let inputTime = document.getElementById('hora');

    inputDate.value = date;
    inputTime.value = time;
});