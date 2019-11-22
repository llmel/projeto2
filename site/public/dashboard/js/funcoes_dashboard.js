username.innerHTML = `${sessionStorage.usuario}`;

function logout() {
  sessionStorage.clear();
  window.location.href = "./login.html"
}

function zerar_notif() {
	qtd_notif.style.display = `none`
}