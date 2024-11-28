const slots_count = document.getElementById('slots-count');
const vehicles_count = document.getElementById('vehicles-count');
const slots_available_count = document.getElementById('slots-available-count');
const message_error = document.getElementById('message-error');

document.addEventListener('DOMContentLoaded', (event) => {
    fetch("http://localhost:5000/api/v1/parking/status", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then(response => response.json())
        .then(data => {
            console.log("Respuesta del servidor:", data);
            if (!data.message) {
                animateCounter('slots-count', 0, data.slots_count, 1100);
                animateCounter('vehicles-count', 0, data.vehicles_count, 1100);
                animateCounter('slots-available-count', 0, data.slots_count - data.vehicles_count, 1100);
            } else {
                message_error.style.display = 'block';
                message_error.textContent = data.message;
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });
});

function animateCounter(id, start, end, duration) {
    const element = document.getElementById(id);
    const range = end - start;
    if (range === 0) return;
    let startTime = null;

    // Agregar clase de animación
    element.classList.add('animated');

    function updateCounter(currentTime) {
        if (!startTime) startTime = currentTime;
        const elapsedTime = currentTime - startTime;
        const progress = Math.min(elapsedTime / duration, 1);
        const currentValue = Math.floor(start + range * progress);
        element.textContent = currentValue;

        if (progress < 1) {
            requestAnimationFrame(updateCounter);
        } else {
            element.classList.remove('animated');
        }
    }
    requestAnimationFrame(updateCounter);
}
