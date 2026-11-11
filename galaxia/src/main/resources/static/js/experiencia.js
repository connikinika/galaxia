// Estrellas que titilan
const lienzo = document.getElementById('estrellas'), pincel = lienzo.getContext('2d');
let estrellas = [];
function ajustar() {
  lienzo.width = innerWidth; lienzo.height = innerHeight;
  estrellas = Array.from({ length: Math.min(240, Math.floor(innerWidth / 4)) }, () => ({
    x: Math.random() * lienzo.width, y: Math.random() * lienzo.height,
    r: Math.random() * 1.5 + .3, v: Math.random() * .25 + .03, f: Math.random() * 6 }));
}
ajustar(); addEventListener('resize', ajustar);
(function dibujar(t) {
  pincel.clearRect(0, 0, lienzo.width, lienzo.height);
  for (const e of estrellas) {
    e.x = (e.x + e.v) % lienzo.width;
    pincel.globalAlpha = .45 + .55 * Math.sin(t / 700 + e.f);
    pincel.fillStyle = '#fff6d6';
    pincel.beginPath(); pincel.arc(e.x, e.y, e.r, 0, 6.3); pincel.fill();
  }
  requestAnimationFrame(dibujar);
})(0);

// Inicio: el navegador pide un toque para reproducir sonido
const inicio = document.getElementById('inicio'), musica = document.getElementById('musica'),
  video = document.getElementById('video'), mute = document.getElementById('mute'),
  frases = [...document.querySelectorAll('#frases li')].map(l => l.textContent);
inicio.onclick = () => {
  inicio.remove();
  if (video) video.play().catch(() => {});
  if (musica) {
    mute.hidden = false; musica.muted = false; musica.volume = 1;
    musica.play().catch(err => aviso('El navegador bloqueó la música (' + err.name + '). Toca el botón 🔇 de arriba.'));
  }
  let i = 0;
  const lanzar = () => {
    if (!frases.length) return;
    const d = document.createElement('div');
    d.className = 'flota'; d.textContent = frases[i++ % frases.length];
    d.style.left = (8 + Math.random() * 46) + 'vw';
    d.style.animationDuration = (9 + Math.random() * 4) + 's';
    document.body.appendChild(d); setTimeout(() => d.remove(), 14000);
  };
  lanzar(); setInterval(lanzar, 3500);
};
if (musica) musica.onerror = () => aviso('No se pudo cargar la música. Prueba con un archivo .mp3 o .m4a.');
mute.onclick = () => {
  if (musica.paused) { musica.muted = false; musica.play().then(() => mute.textContent = '🔊').catch(() => aviso('Sigue bloqueada: revisa el volumen del dispositivo.')); return; }
  musica.muted = !musica.muted; mute.textContent = musica.muted ? '🔇' : '🔊';
};
function aviso(texto) {
  const a = document.createElement('div'); a.className = 'aviso-escena'; a.textContent = texto;
  document.body.appendChild(a); setTimeout(() => a.remove(), 6000);
}

// Toca la pantalla: salen flores
document.addEventListener('pointerdown', e => {
  if (inicio.isConnected || e.target.closest('button,a')) return;
  for (let k = 0; k < 6; k++) {
    const c = document.createElement('span');
    c.className = 'chispa'; c.textContent = document.getElementById('emoji').textContent;
    c.style.left = e.clientX + 'px'; c.style.top = e.clientY + 'px';
    c.style.setProperty('--dx', (Math.random() * 160 - 80) + 'px');
    c.style.setProperty('--dy', (-60 - Math.random() * 120) + 'px');
    document.body.appendChild(c); setTimeout(() => c.remove(), 1400);
  }
});
