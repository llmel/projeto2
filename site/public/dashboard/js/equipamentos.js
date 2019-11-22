var tipo_atual_tela = 'c'

function mudar_tipo_tela (t) {

	tipo_atual_tela = t;
	if (t == 'i') {
		div_botoes.innerHTML = `
			<button onclick="inserir_equipamentos()" type="button" class="btn btn-primary" id="btn_executar">Executar</button>

	        <button onclick="mudar_tipo_tela('i')" type="button" class="btn btn-info" id="btn_inserir">Inserção</button>

	        <button onclick="mudar_tipo_tela('c')" type="button" class="btn btn-info" id="btn_consultar">Consulta</button>
		`
		btn_inserir.disabled = true;
		btn_consultar.disabled = false;
		tbl_equipamentos.classList.remove('table');
		tbl_equipamentos.innerHTML = `
			<tr>

			  	<th> ID Equipamento </th>
              	<th> Descrição </th>
              	<th> IP </th>
              	<th> Relevância </th>
              	<th> Empresa </th>
              	<th> Área </th>

            </tr>
			<tr>
				<td> <input id="id" type="text"> </td>
				<td> <input id="descricao" type="text"> </td>
				<td> <input id="ip" type="text"> </td>
				<td> <input id="relevancia" type="text"> </td>
				<td> <input id="empresa" type="text"> </td>
				<td> <input id="area" type="text"> </td>
			</tr>
		`
		} else if (t == 'c') {
			div_botoes.innerHTML = `
				<button onclick="consultar_equipamentos()" type="button" class="btn btn-primary" id="btn_executar">Executar</button>

		        <button onclick="mudar_tipo_tela('i')" type="button" class="btn btn-info" id="btn_inserir">Inserção</button>

		        <button onclick="mudar_tipo_tela('c')" type="button" class="btn btn-info" id="btn_consultar">Consulta</button>
			`
			btn_inserir.disabled = false;
			btn_consultar.disabled = true;
			tbl_equipamentos.classList.add('table');
			tbl_equipamentos.innerHTML = `
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

function inserir_equipamentos () {
	var dados = {
			id:(id.value)*1,
			descricao:descricao.value,
			ip:ip.value,
			relevancia:relevancia.value,
			empresa:empresa.value,
			area:area.value
		}
		
	fetch('/consultas/inserir_equipamentos',{
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

function consultar_equipamentos () {
	var dados = {
			empresa:combo_empresas.value,
		}

	fetch('/consultas/consultar_equipamentos',{
		method: "POST",
		body: new URLSearchParams(dados)
	}).then((resposta) => {
		if (resposta.ok) {
			resposta.json().then((resultado) => {

				tbl_equipamentos.innerHTML = `
					<tr>

			          	<th> ID Equipamento </th>
		              	<th> Descrição </th>
		              	<th> IP </th>
		              	<th> Relevância </th>

			        </tr>
					`

				for (c = 0;c < resultado.length;c++) {
					tbl_equipamentos.innerHTML += `
					<tr>
						<td> ${resultado[c].id} </td>
						<td> ${resultado[c].descricao} </td>
						<td> ${resultado[c].ip} </td>
						<td> ${resultado[c].relevancia} </td>
					</tr>
					`
					}

			})

		} else {
			console.log("Errooooo")
		}
	})
}
