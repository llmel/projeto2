function relatorio(dtInicio,dtFim) {
	// alert(String(dtInicio.value) + " E " + String(dtFim.value));
	var d_i = String(dtInicio.value);     // d_i - data início
	var d_f = String(dtFim.value);        // d_f - data fim

	var datas = {
		dtInicio: d_i,
		dtFim: d_f
	}

	// alert(JSON.stringify(datas));

	fetch('/consultas/relatorios', {
		method: "POST",
		body: new URLSearchParams(datas)
	}).then(function (response) {
  	if (response.ok) {

    	response.json().then(function (resposta) {

    		tabela.innerHTML = `
    		    <tr>

              <th id="id"> ID GMUD </th>
              <th id="motivo"> Motivo </th>
              <th id="categoria"> Categoria </th>
              <th id="data"> Data </th>
              <th id="detalhes"> + </th>

            </tr>
    		`

    		for (c = 0;c < resposta.length;c++) {
    			tabela.innerHTML += `
    			<tr>
    				<td> ${resposta[c].idgmud} </td>
    				<td> ${resposta[c].motivo} </td>
    				<td> ${resposta[c].categoria} </td>
    				<td> ${resposta[c].data} </td>
    			</tr>
    			`
    		}

	    });

  	} else {
    console.log('Erro na consulta!');
  		}
	});

}

function gerar_pdf () {
        var minhaTabela = document.getElementById('div_tabela').innerHTML;

        var style = "<style>";
        style = style + "table {width: 100%;font: 20px Calibri;}";
        style = style + "table, th, td {border: solid 1px #DDD; border-collapse: collapse;";
        style = style + "padding: 2px 3px;text-align: center;}";
        style = style + "</style>";

        // CRIA UM OBJETO WINDOW
        var win = window.open('', '', 'height=500,width=500');

        win.document.write('<html><head>');
        win.document.write('<title>GMUDS</title>');   // <title> CABEÇALHO DO PDF.
        win.document.write(style);                                     // INCLUI UM ESTILO NA TAB HEAD
        win.document.write('</head>');
        win.document.write('<body>');
        win.document.write(minhaTabela);                          // O CONTEUDO DA TABELA DENTRO DA TAG BODY
        win.document.write('</body></html>');

        win.print();                                                            // IMPRIME O CONTEUDO

        win.close();                                            // FECHA A JANELA

        
}