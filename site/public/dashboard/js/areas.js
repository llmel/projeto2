var tipo_atual_tela = 'c'

function mudar_tipo_tela (t) {

	tipo_atual_tela = t;
	if (t == 'i') {
		div_botoes.innerHTML = `
			<button onclick="inserir_areas()" type="button" class="btn btn-primary" id="btn_executar">Executar</button>

	        <button onclick="mudar_tipo_tela('i')" type="button" class="btn btn-info" id="btn_inserir">Inserção</button>

	        <button onclick="mudar_tipo_tela('c')" type="button" class="btn btn-info" id="btn_consultar">Consulta</button>
		`
		btn_inserir.disabled = true;
		btn_consultar.disabled = false;
		tbl_areas.innerHTML = `
			<tr>

			  	<th> idempresa </th>
              	<th> IDArea </th>
              	<th> NomeArea </th>

            </tr>
			<tr>
				<td> <input id="empresa" type="text"> </td>
				<td> <input id="area" type="text"> </td>
				<td> <input id="nome" type="text"> </td>
			</tr>
		`
		} else if (t == 'c') {
			div_botoes.innerHTML = `
				<button onclick="consultar_areas()" type="button" class="btn btn-primary" id="btn_executar">Executar</button>

		        <button onclick="mudar_tipo_tela('i')" type="button" class="btn btn-info" id="btn_inserir">Inserção</button>

		        <button onclick="mudar_tipo_tela('c')" type="button" class="btn btn-info" id="btn_consultar">Consulta</button>
			`
			btn_inserir.disabled = false;
			btn_consultar.disabled = true;
			tbl_areas.innerHTML = `
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


function inserir_areas () {
	var dados = {
			empresa:(empresa.value)*1,
			area:(area.value)*1,
			nome:nome.value
		}
		
	fetch('/consultas/inserir_areas',{
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

function consultar_areas () {
	var dados = {
			empresa:combo_empresas.value,
		}
		
	fetch('/consultas/consultar_areas',{
		method: "POST",
		body: new URLSearchParams(dados)
	}).then((resposta) => {
		if (resposta.ok) {
			resposta.json().then(resultado => {

				if (resultado.length == 0) {
					alert("Nenhum resultado encontrado na consulta")
					} else {
						tbl_areas.innerHTML = `
							<tr>

				              <th> IDArea </th>
				              <th> NomeArea </th>
				              <th> NomeEmpresa </th>
				              <th> IDEmpresa </th>

				            </tr>
				        `;
				        for (c = 0; c < resultado.length;c++) {
				        	tbl_areas.innerHTML += `
					            <tr>
					            	<td> ${resultado[c].idareas} </td>
					            	<td> ${resultado[c].nomearea} </td>
					            	<td> ${resultado[c].nomeEmpresa} </td>
					            	<td> ${resultado[c].idempresa} </td>
					            </tr>
							`
				        }
				        btn_consultar.disabled = false; 
					}
			})
			

		} else {

			alert("Errrooooo")
		
		}
	})
}


function consultar_func () {
	fetch('/consultas/funcionarios',{
		method: "POST",
		body: new URLSearchParams()
	}).then((resposta) => {
		if (resposta.ok) {
			resposta.json().then((resultado) => {

				tbl_funcionarios.innerHTML = `
					<tr>

			          <th id="id"> ID GMUD </th>
			          <th id="motivo"> Motivo </th>
			          <th id="categoria"> Categoria </th>
			          <th id="data"> Data </th>
			          <th id="detalhes"> + </th>

			        </tr>
					`

				for (c = 0;c < resposta.length;c++) {
					tbl_funcionarios.innerHTML += `
					<tr>
						<td> ${resposta[c].idfuncionario} </td>
						<td> ${resposta[c].nomefuncionario} </td>
						<td> ${resposta[c].cargo} </td>
						<td> ${resposta[c].login} </td>
					</tr>
					`
					}

			})

		} else {
			console.log("Errooooo")
		}
	})
}

function consultar_equip () {
	fetch('/consultas/equipamentos',{
		method: "POST",
		body: new URLSearchParams()
	}).then((resposta) => {
		if (resposta.ok) {
			resposta.json().then((resultado) => {

				tbl_equipamentos.innerHTML = `
					<tr>

			          <th id="id"> ID GMUD </th>
			          <th id="motivo"> Motivo </th>
			          <th id="categoria"> Categoria </th>
			          <th id="data"> Data </th>
			          <th id="detalhes"> + </th>

			        </tr>
					`

				for (c = 0;c < resposta.length;c++) {
					tbl_equipamentos.innerHTML += `
					<tr>
						<td> ${resposta[c].idequipamento} </td>
						<td> ${resposta[c].ipequipamento} </td>
						<td> ${resposta[c].discricao} </td>
						<td> ${resposta[c].relevancia} </td>
					</tr>
					`
					}

			})

		} else {
			console.log("Errooooo")
		}
	})
}