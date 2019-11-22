var qtd_dias_mes = [31,28,31,30,31,30,31,31,30,31,30,31]
var dias_da_semana = ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado']
var meses_do_ano = ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'];
var results = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];

var data = new Date();
var dia = data.getDate();
var dia_da_semana = data.getDay();
var mes = data.getMonth()+1;
var ano = data.getFullYear();

function consultar(a) {
	console.log(a);
	if (a.mes == mes) {
		a.dia = dia
	}
	var b = a;
	fetch('/consultas/agendadas',{
		method: "POST",
		body: new URLSearchParams(b)
	}).then((resposta) => {
		if (resposta.ok) {
			resposta.json().then((resultado) => {

				
				for (c = 0;c < results.length;c++) {
					results[c] = 0;
				}
			
				console.log(resultado[0].date);
				for (c = 0;c < resultado.length;c++) {
					for (d = 1;d <= 31;d++) {
						if (resultado[c].date == d) {
							results[d-1] += 1;
							break;
							}
						}
					}
					
			})
			
			
			dias_mes(a.mes-1);

			console.log(results);

		} else {
			console.log("Errooooo")
		}
	})
}

function mostrar_trs () {
	results[0] > 0 ? td1.innerHTML += `<br> <i onclick="detalhes_gmud(1)" class="fa fa-search-plus"></i>` : td1.innerHTML += `<br> ---`
	results[1] > 0 ? td2.innerHTML += `<br> <i onclick="detalhes_gmud(2)" class="fa fa-search-plus"></i>` : td2.innerHTML += `<br> ---`
	results[2] > 0 ? td3.innerHTML += `<br> <i onclick="detalhes_gmud(3)" class="fa fa-search-plus"></i>` : td3.innerHTML += `<br> ---`
	results[3] > 0 ? td4.innerHTML += `<br> <i onclick="detalhes_gmud(4)" class="fa fa-search-plus"></i>` : td4.innerHTML += `<br> ---`
	results[4] > 0 ? td5.innerHTML += `<br> <i onclick="detalhes_gmud(5)" class="fa fa-search-plus"></i>` : td5.innerHTML += `<br> ---`
	results[5] > 0 ? td6.innerHTML += `<br> <i onclick="detalhes_gmud(6)" class="fa fa-search-plus"></i>` : td6.innerHTML += `<br> ---`
	results[6] > 0 ? td7.innerHTML += `<br> <i onclick="detalhes_gmud(7)" class="fa fa-search-plus"></i>` : td7.innerHTML += `<br> ---`
	results[7] > 0 ? td8.innerHTML += `<br> <i onclick="detalhes_gmud(8)" class="fa fa-search-plus"></i>` : td8.innerHTML += `<br> ---`
	results[8] > 0 ? td9.innerHTML += `<br> <i onclick="detalhes_gmud(9)" class="fa fa-search-plus"></i>` : td9.innerHTML += `<br> ---`
	results[9] > 0 ? td10.innerHTML += `<br> <i onclick="detalhes_gmud(10)" class="fa fa-search-plus"></i>` : td10.innerHTML += `<br> ---`
	results[10] > 0 ? td11.innerHTML += `<br> <i onclick="detalhes_gmud(11)" class="fa fa-search-plus"></i>` : td11.innerHTML += `<br> ---`
	results[11] > 0 ? td12.innerHTML += `<br> <i onclick="detalhes_gmud(12)" class="fa fa-search-plus"></i>` : td12.innerHTML += `<br> ---`
	results[12] > 0 ? td13.innerHTML += `<br> <i onclick="detalhes_gmud(13)" class="fa fa-search-plus"></i>` : td13.innerHTML += `<br> ---`
	results[13] > 0 ? td14.innerHTML += `<br> <i onclick="detalhes_gmud(14)" class="fa fa-search-plus"></i>` : td14.innerHTML += `<br> ---`
	results[14] > 0 ? td15.innerHTML += `<br> <i onclick="detalhes_gmud(15)" class="fa fa-search-plus"></i>` : td15.innerHTML += `<br> ---`
	results[15] > 0 ? td16.innerHTML += `<br> <i onclick="detalhes_gmud(16)" class="fa fa-search-plus"></i>` : td16.innerHTML += `<br> ---`
	results[16] > 0 ? td17.innerHTML += `<br> <i onclick="detalhes_gmud(17)" class="fa fa-search-plus"></i>` : td17.innerHTML += `<br> ---`
	results[17] > 0 ? td18.innerHTML += `<br> <i onclick="detalhes_gmud(18)" class="fa fa-search-plus"></i>` : td18.innerHTML += `<br> ---`
	results[18] > 0 ? td19.innerHTML += `<br> <i onclick="detalhes_gmud(19)" class="fa fa-search-plus"></i>` : td19.innerHTML += `<br> ---`
	results[19] > 0 ? td20.innerHTML += `<br> <i onclick="detalhes_gmud(20)" class="fa fa-search-plus"></i>` : td20.innerHTML += `<br> ---`
	results[20] > 0 ? td21.innerHTML += `<br> <i onclick="detalhes_gmud(21)" class="fa fa-search-plus"></i>` : td21.innerHTML += `<br> ---`
	results[21] > 0 ? td22.innerHTML += `<br> <i onclick="detalhes_gmud(22)" class="fa fa-search-plus"></i>` : td22.innerHTML += `<br> ---`
	results[22] > 0 ? td23.innerHTML += `<br> <i onclick="detalhes_gmud(23)" class="fa fa-search-plus"></i>` : td23.innerHTML += `<br> ---`
	results[23] > 0 ? td24.innerHTML += `<br> <i onclick="detalhes_gmud(24)" class="fa fa-search-plus"></i>` : td24.innerHTML += `<br> ---`
	results[24] > 0 ? td25.innerHTML += `<br> <i onclick="detalhes_gmud(25)" class="fa fa-search-plus"></i>` : td25.innerHTML += `<br> ---`
	results[25] > 0 ? td26.innerHTML += `<br> <i onclick="detalhes_gmud(26)" class="fa fa-search-plus"></i>` : td26.innerHTML += `<br> ---`
	results[26] > 0 ? td27.innerHTML += `<br> <i onclick="detalhes_gmud(27)" class="fa fa-search-plus"></i>` : td27.innerHTML += `<br> ---`
	results[27] > 0 ? td28.innerHTML += `<br> <i onclick="detalhes_gmud(28)" class="fa fa-search-plus"></i>` : td28.innerHTML += `<br> ---`
	results[28] > 0 ? td29.innerHTML += `<br> <i onclick="detalhes_gmud(29)" class="fa fa-search-plus"></i>` : td29.innerHTML += `<br> ---`
	results[29] > 0 ? td30.innerHTML += `<br> <i onclick="detalhes_gmud(30)" class="fa fa-search-plus"></i>` : td30.innerHTML += `<br> ---`
	results[30] > 0 ? td31.innerHTML += `<br> <i onclick="detalhes_gmud(31)" class="fa fa-search-plus"></i>` : td31.innerHTML += `<br> ---`
}

	

function verifi_date() {

	for (c = mes-1; c < meses_do_ano.length;c++) {
	combo_mes.innerHTML += `
		<option value="${c}"> ${meses_do_ano[c]} </option>
	`
	}

	combo_ano.innerHTML += `
		<option value="${ano}"> ${ano} </option>
		<option value="${ano+1}"> ${ano+1} </option>
	`

	combo_mes.value = mes-1;
	combo_ano.value = ano;

	var json = {
		dia: dia,
		mes: mes,
		ano: ano
	}

	consultar(json);
	// dias_mes(mes);

}

function dias_mes (matual) {

	var dias_mes_atual = 1;

	l1.innerHTML = "";
	l2.innerHTML = "";
	l3.innerHTML = "";
	l4.innerHTML = "";
	l5.innerHTML = "";

	// Preenche dias do mês atual na linha 1
	for (c = 0;c <= 6;c++) {
	l1.innerHTML += `
		<td id="${"td" + dias_mes_atual}"> ${dias_mes_atual} </td>
	`
	dias_mes_atual++;
	}

	// Preenche dias do mês atual na linha 2
	for (c = 0;c <= 6;c++) {
		l2.innerHTML += `
			<td id="${"td" + dias_mes_atual}"> ${dias_mes_atual} </td>
		`
		dias_mes_atual++;
	}

	// Preenche dias do mês atual na linha 3
	for (c = 0;c <= 6;c++) {
		l3.innerHTML += `
			<td id="${"td" + dias_mes_atual}"> ${dias_mes_atual} </td>
		`
		dias_mes_atual++;
	}

	// Preenche dias do mês atual na linha 4
	for (c = 0;c <= 6;c++) {
		l4.innerHTML += `
			<td id="${"td" + dias_mes_atual}"> ${dias_mes_atual} </td>
		`
		dias_mes_atual++;
	}

	// Preenche dias do mês atual na linha 5
	for (c = 0;c <= 6;c++) {
		if (dias_mes_atual <= qtd_dias_mes[matual]) {
			l5.innerHTML += `
			<td id="${"td" + dias_mes_atual}"> ${dias_mes_atual} </td>
			`
			dias_mes_atual++;
		}
		
	}

	setTimeout(mostrar_trs, 5000);

}

function mudar_combo_mes (p) {
	if (p == ano) {
		combo_mes.innerHTML = "";
		for (c = mes-1; c < meses_do_ano.length;c++) {
			combo_mes.innerHTML += `
				<option value="${c}"> ${meses_do_ano[c]} </option>
			`
		}
	} else {
		combo_mes.innerHTML = "";
		for (c = 0;c < meses_do_ano.length;c++) {
			combo_mes.innerHTML += `
				<option value="${c}"> ${meses_do_ano[c]} </option>
			`
		}
	}
	consultar({dia:1,mes:combo_mes.value*1+1,ano:combo_ano.value})
}

function modal () {
	$('#ExemploModalCentralizado').modal('show')
}


function detalhes_gmud (dia) {
	conteudo_modal.innerHTML = `
					<img id="gif_carregando" style="margin-left:80px" src="img/aguarde.gif">
				`
	modal();
	var detalhes = {
		dia: dia*1,
		mes: combo_mes.value*1+1,
		ano: combo_ano.value*1
	}
	fetch('/consultas/detalhes_gmud',{
		method: "POST",
		body: new URLSearchParams(detalhes)
	}).then(resposta => {
		if (resposta.ok) {
			resposta.json().then((resultado) => {
				conteudo_modal.innerHTML = `
					ID = ${resultado[0].gmud} <br>
					Motivo = ${resultado[0].motivo}
				`
				console.log(resultado[0]);
				gif_carregando.style.display = "none";
			})
		} else {
			console.log ("Errooooo");
		}
	})
}
