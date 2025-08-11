const video = document.getElementById('video');
const canvas = document.getElementById('canvas');
const snapBtn = document.getElementById('snap');
const downloadBtn = document.getElementById('download');
const sendBtn = document.getElementById('send');
const cropArea = document.getElementById('cropArea');
const outputArea = document.getElementById('outputArea');
const notificationBox = document.getElementById('notification-box');

const ctx = canvas.getContext('2d');

function showNotification(message, type = 'error') {
    notificationBox.textContent = message;
    notificationBox.className = 'notification'; 
    notificationBox.classList.add(type);      
    notificationBox.classList.add('show');

    
    setTimeout(() => {
        notificationBox.classList.remove('show');
    }, 3000);
}


async function initWebcam() {
    try {
        const stream = await navigator.mediaDevices.getUserMedia({ video: true });
        video.srcObject = stream;
    } catch (err) {
        console.error("Erro ao acessar webcam: ", err);
        showNotification('Erro ao acessar webcam. Verifique as permissões.');
    }
}

let capturedImageBase64 = null;

snapBtn.addEventListener('click', () => {
    const videoRect = video.getBoundingClientRect();
    const cropRect = cropArea.getBoundingClientRect();

    const scaleX = video.videoWidth / videoRect.width;
    const scaleY = video.videoHeight / videoRect.height;

    const sx = (cropRect.left - videoRect.left) * scaleX;
    const sy = (cropRect.top - videoRect.top) * scaleY;
    const sw = cropRect.width * scaleX;
    const sh = cropRect.height * scaleY;

    canvas.width = sw;
    canvas.height = sh;

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.drawImage(video, sx, sy, sw, sh, 0, 0, sw, sh);

    capturedImageBase64 = canvas.toDataURL('image/png').split(',')[1];

    outputArea.style.display = 'block';
    downloadBtn.disabled = false;
    sendBtn.disabled = false;

    showNotification('Foto capturada com sucesso!', 'success');
});


downloadBtn.addEventListener('click', () => {
    if (!capturedImageBase64) {
        showNotification('Nenhuma foto capturada para baixar!');
        return;
    }
    const imageData = 'data:image/png;base64,' + capturedImageBase64;
    const link = document.createElement('a');
    link.href = imageData;
    link.download = 'captura-vaga-certa.png';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
});


sendBtn.addEventListener('click', async () => {
    if (!capturedImageBase64) {
        showNotification('Nenhuma foto capturada para enviar!');
        return;
    }

    sendBtn.disabled = true;
    showNotification('Enviando foto...', 'success');

    try {
        const response = await fetch('/upload', {   
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ image: capturedImageBase64 })
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Falha no envio da imagem. Status: ${response.status}. Resposta: ${errorText}`);
        }

        const data = await response.json();

        if (data.status === 'ok') {
            showNotification('Upload realizado com sucesso!', 'success');
        } else {
            throw new Error(data.message || 'Erro no upload');
        }

    } catch (err) {
        console.error('Erro no upload:', err);
        showNotification(`Erro ao enviar a imagem: ${err.message}`);
    } finally {
        sendBtn.disabled = false;
    }
});

initWebcam();
