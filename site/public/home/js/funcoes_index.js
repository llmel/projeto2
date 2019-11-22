logged_not_logged();

function logged_not_logged() {
	if (sessionStorage.usuario != undefined) {
		/*
		myNavbar.innerHTML = `
			<ul id="navbarlegal" class="nav navbar-nav navbar-right">
			<li class="active"><a href="#main-header">Início</a></li>
	        <li class=""><a href="#feature">Sobre Nós</a></li>
	        <li class=""><a href="#service">Nosso Serviço</a></li>
	        <li class=""><a href="#contact">Contato</a></li>
	        <li class="dropdown">
	            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
	                <span class="">${sessionStorage.usuario}</span>
	                <span class="caret"></span>
	            </a>
	            <ul class="dropdown-menu extended logout">
	              <div class="log-arrow-up"></div>
	              <li class="eborder-top">
	                <a href="./dashboard/index.html"><i class="icon_profile"></i> Dashboard </a>
	              </li>
	              <li>
	                <a href="#" onclick="logout()"><i class="icon_key_alt"></i> Logoff</a>
	              </li>
	            </ul>
          	</li>
          </ul>
		`
		*/
	}
}

function logout() {
	sessionStorage.clear();
	window.location.href = "./index.html"
}

