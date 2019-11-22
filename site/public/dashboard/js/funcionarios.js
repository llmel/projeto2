var tipo_atual_tela = 'c'

function mudar_tipo_tela (t) {

	tipo_atual_tela = t;
	if (t == 'i') {
		div_botoes.innerHTML = `
			<button onclick="inserir_funcionarios()" type="button" class="btn btn-primary" id="btn_executar">Executar</button>

	        <button onclick="mudar_tipo_tela('i')" type="button" class="btn btn-info" id="btn_inserir">Inserção</button>

	        <button onclick="mudar_tipo_tela('c')" type="button" class="btn btn-info" id="btn_consultar">Consulta</button>
		`
		btn_inserir.disabled = true;
		btn_consultar.disabled = false;
		tbl_funcionarios.classList.remove('table');
		tbl_funcionarios.innerHTML = `
			<tr>

			  	<th> ID </th>
              	<th> Nome </th>
              	<th> Cargo </th>
              	<th> Login </th>
              	<th> Senha </th>
              	<th> Empresa </th>


            </tr>
			
				<td> <input id="id" type="text"> </td>
				<td> <input id="nome" type="text"> </td>
				<td> <input id="cargo" type="text"> </td>
				<td> <input id="login" type="text"> </td>
				<td> <input id="senha" type="password"> </td>
				<td> <input id="empresa" type="text"> </td>
			
		`
		} else if (t == 'c') {
			div_botoes.innerHTML = `
				<button onclick="consultar_funcionarios()" type="button" class="btn btn-primary" id="btn_executar">Executar</button>

		        <button onclick="mudar_tipo_tela('i')" type="button" class="btn btn-info" id="btn_inserir">Inserção</button>

		        <button onclick="mudar_tipo_tela('c')" type="button" class="btn btn-info" id="btn_consultar">Consulta</button>
			`
			btn_inserir.disabled = false;
			btn_consultar.disabled = true;
			tbl_funcionarios.classList.add('table');
			tbl_funcionarios.innerHTML = `
			<tr>

				<th> NomeEmpresa </th>

            </tr>
			<tr>
				<td> <select id="combo_empresas" > </select> </td>
			</tr>
			`
			setTimeout(carregar_combo,3000);
			}
}

function carregar_combo () {
	fetch('/consultas/combo_empresas',{
		method: "POST",
		body: "teste"
	}).then((resposta) => {
		if (resposta.ok) {
			resposta.json().then((resultado) => {
				combo_empresas.innerHTML = `<option> Escolha a empresa </option>`
				for (c = 0;c < resultado.length;c++) {
					combo_empresas.innerHTML += `
						<option value="${resultado[c].idempresa}"> ${resultado[c].nomeempresa} </option>
					`
					}

			})

		} else {
			console.log("Errooooo")
		}
	})
}

function inserir_funcionarios () {
	var dados = {
			id:(id.value)*1,
			nome:nome.value,
			cargo:cargo.value,
			login:login.value,
			senha:senha.value,
			empresa:empresa.value
		}
		
	fetch('/consultas/inserir_funcionarios',{
		method: "POST",
		body: new URLSearchParams(dados)
	}).then((resposta) => {
		if (resposta.ok) {

			alert("Gravação Efetuada com Sucesso");

		} else {

			resposta.text().then(resp => {
				alert(resp);
			})
		
		}
	})
}

function consultar_funcionarios () {
	var dados = {
			empresa:combo_empresas.value,
		}

	fetch('/consultas/consultar_funcionarios',{
		method: "POST",
		body: new URLSearchParams(dados)
	}).then((resposta) => {
		if (resposta.ok) {
			resposta.json().then((resultado) => {

				tbl_funcionarios.innerHTML = `
					<tr>

			          	<th> ID </th>
		              	<th> Nome </th>
		              	<th> Cargo </th>
		              	<th> Login </th>

			        </tr>
					`

				for (c = 0;c < resultado.length;c++) {
					tbl_funcionarios.innerHTML += `
					<tr>
						<td> ${resultado[c].id} </td>
						<td> ${resultado[c].nome} </td>
						<td> ${resultado[c].cargo} </td>
						<td> ${resultado[c].login} </td>
					</tr>
					`
					}

			})

		} else {
			console.log("Errooooo")
		}
	})
}
