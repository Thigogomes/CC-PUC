const video = document.getElementById('video');
const canvas = document.getElementById('canvas');
const ctx = canvas.getContext('2d');
const snapBtn = document.getElementById('snap');
const downloadBtn = document.getElementById('download');
const sendBtn = document.getElementById('send');
const cropArea = document.getElementById('cropArea');

navigator.mediaDevices.getUserMedia({ video: true })
  .then(stream => {
    video.srcObject = stream;
  })
  .catch(err => {
    alert('Erro ao acessar webcam: ' + err);
  });

let capturedImageBase64 = null;

snapBtn.addEventListener('click', () => {
  const cropRect = cropArea.getBoundingClientRect();
  const videoRect = video.getBoundingClientRect();

  const scaleX = video.videoWidth / videoRect.width;
  const scaleY = video.videoHeight / videoRect.height;

  const sx = (cropRect.left - videoRect.left) * scaleX;
  const sy = (cropRect.top - videoRect.top) * scaleY;
  const sw = cropRect.width * scaleX;
  const sh = cropRect.height * scaleY;

  canvas.width = sw;
  canvas.height = sh;

  ctx.drawImage(video, sx, sy, sw, sh, 0, 0, sw, sh);

  capturedImageBase64 = canvas.toDataURL('image/png').split(',')[1];
});

downloadBtn.addEventListener('click', () => {
  if (!capturedImageBase64) {
    alert('Tire a foto primeiro!');
    return;
  }
  const imageData = 'data:image/png;base64,' + capturedImageBase64;
  const link = document.createElement('a');
  link.href = imageData;
  link.download = 'foto-recorte.png';
  link.click();
});

sendBtn.addEventListener('click', () => {
  if (!capturedImageBase64) {
    alert('Tire a foto primeiro!');
    return;
  }

  fetch('/upload', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ image: capturedImageBase64 })
  })
  .then(res => res.json())
  .then(data => {
    if(data.status === 'ok') {
      alert('Upload realizado com sucesso: ' + data.file);
    } else {
      alert('Erro no upload: ' + data.message);
    }
  })
  .catch(err => alert('Erro ao enviar a imagem: ' + err));
});
