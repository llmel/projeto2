username.innerHTML = `${sessionStorage.usuario}`;
ta_username.innerHTML = `${sessionStorage.usuario}`;

function logout() {
  sessionStorage.clear();
  window.location.href = "./login.html"
}

function zerar_notif() {
	qtd_notif.style.display = `none`
}

function buscar_dados () {
	aguardar();
	gmuds_andamento();
	setTimeout(sete_dias, 7000);
}

function aguardar () {
	$('#modal_aguardar').modal('show')
}

function gmuds_andamento () {
	fetch(`/leituras/andamento/${sessionStorage.empresa}`,{
		method: "GET"
	}).then(resposta => {
		if (resposta.ok) {
			resposta.json().then(resultado => {
				div_gmuds_andamento.innerHTML = resultado.gmuds_andamento;
			})
		} else {
			console.log("Deu ruim");
		}
		areas_afetadas();
	})
}

function areas_afetadas () {
	fetch(`/leituras/a_afetadas/${sessionStorage.empresa}`,{
		method: "GET"
	}).then(resposta => {
		if (resposta.ok) {
			resposta.json().then(resultado => {
				div_areas_afetadas.innerHTML = resultado.areas_afetadas;
			})
		} else {
			console.log("Deu ruim");
		}
		equip_parados();
	})
}

function equip_parados () {
	fetch(`/leituras/e_afetados/${sessionStorage.empresa}`,{
		method: "GET"
	}).then(resposta => {
		if (resposta.ok) {
			resposta.json().then(resultado => {
				div_equip_parados.innerHTML = resultado.equip_parados;
			})
		} else {
			console.log("Deu ruim");
		}
		gmuds_concluidas();
	})
}

function gmuds_concluidas () {
	fetch(`/leituras/concluidas/${sessionStorage.empresa}`,{
		method: "GET"
	}).then(resposta => {
		if (resposta.ok) {
			resposta.json().then(resultado => {
				div_gmuds_concluidas.innerHTML = resultado.gmuds_concluidas;
			})
		} else {
			console.log("Deu ruim");
		}
    gmuds_agendadas();
	})
}

function gmuds_agendadas () {
  fetch(`/leituras/agendadas/${sessionStorage.empresa}`,{
    method: "GET"
  }).then(resposta => {
    if (resposta.ok) {
      resposta.json().then(resultado => {
        div_gmuds_agendadas.innerHTML = resultado.gmuds_planejadas;
      })
    } else {
      console.log("Deu ruim");
    }
  })
}

function sete_dias () {
	fetch(`/leituras/sete_dias/${sessionStorage.empresa}`,{
		method:"GET"
	}).then(resposta => {
		if (resposta.ok) {
			resposta.json().then(resultado => {
        for (c = 0;c < resultado.length; c++) {
					tbl_sete_dias.innerHTML += `
						<tr>
							<td id="idgmud+${c}">${resultado[c].id}</td>
	                        <td>${resultado[c].area_afetada}</td>
	                        <td>${resultado[c].motivo}</td>
	                        <td>${resultado[c].categoria_gmud}</td>
	                        <td>${resultado[c].data_conclusao}</td>
	                        <td>
	                          <div class="btn-group">
	                            <button onclick="detalhar_gmud(${resultado[c].id})" class="btn btn-success" href="#"><i class="fas fa-search"></i></i></button>
	                            
	                          </div>
	                        </td>
	                     </tr>
					`
				}
				
			})
		} else {
			console.log("Deu ruim");
		}
		mes_a_mes_buscar();
	})
}

var qtd = [0,0,0,0,0,0,0,0,0,0,0,0]

function mes_a_mes_buscar () {
	
    fetch(`/leituras/mes_a_mes/${sessionStorage.empresa}`,{
    	method: "GET"
    }).then(resposta => {
    	if (resposta.ok) {
    		resposta.json().then(resultado => {
    			for(c = 0;c < resultado.length;c++) {
    				if (resultado[c].meses == 1) {
    					qtd[0] = resultado[c].qtd_by_month;
    				} else if (resultado[c].meses == 2) {
    					qtd[1] = resultado[c].qtd_by_month;
    				} else if (resultado[c].meses == 3) {
    					qtd[2] = resultado[c].qtd_by_month;
    				} else if (resultado[c].meses == 4) {
    					qtd[3] = resultado[c].qtd_by_month;
    				} else if (resultado[c].meses == 5) {
    					qtd[4] = resultado[c].qtd_by_month;
    				} else if (resultado[c].meses == 6) {
    					qtd[5] = resultado[c].qtd_by_month;
    				} else if (resultado[c].meses == 7) {
    					qtd[6] = resultado[c].qtd_by_month;
    				} else if (resultado[c].meses == 8) {
    					qtd[7] = resultado[c].qtd_by_month;
    				} else if (resultado[c].meses == 9) {
    					qtd[8] = resultado[c].qtd_by_month;
    				} else if (resultado[c].meses == 10) {
    					qtd[9] = resultado[c].qtd_by_month;
    				} else if (resultado[c].meses == 11) {
    					qtd[10] = resultado[c].qtd_by_month;
    				} else if (resultado[c].meses == 12) {
    					qtd[11] = resultado[c].qtd_by_month;
    				}
    			}
    			console.log(qtd);	
    		})
    	} else {
    		console.log("Deu errooo");
    	}
    	setTimeout(daataa, 3000);
    	mes_area_buscar();
    })
}

function daataa () {
	var dados = {
		labels: ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"],
    	datasets: [{
      		label: "Quantidade",
      		backgroundColor: ['#B22222','#FF0000','#FF6347' ,'#FF4500','#FFA500','#FFD700','#B22222','#FF0000','#FF6347' ,'#FF4500','#FFA500','#FFD700'],
      		hoverBackgroundColor: "#C71585",
     		 borderColor: "#C71585",
      		data: qtd,
    	}]}
    mes_a_mes_plotar(dados);
}

function mes_a_mes_plotar (dadoss) {
    var ctx = document.getElementById("myBarChart");
    window.grafico = new Chart.Bar(ctx, {
        data: dadoss,
        
    // Parte Fixa de configuração do gráfico, não precisa alterar
        options: {
    maintainAspectRatio: false,
    layout: {
      padding: {
        left: 10,
        right: 25,
        top: 25,
        bottom: 0
      }
    },
    scales: {
      xAxes: [{
        time: {
          unit: 'month'
        },
        gridLines: {
          display: false,
          drawBorder: false
        },
        ticks: {
          maxTicksLimit: 12
        },
        maxBarThickness: 35,
      }],
      yAxes: [{
        ticks: {
          min: 0,
          max: 100,
          maxTicksLimit: 20,
          padding: 20,
          // Include a dollar sign in the ticks
          callback: function(value, index, values) {
            return  + number_format(value);
          }
        },
        gridLines: {
          color: "rgb(234, 236, 244)",
          zeroLineColor: "rgb(234, 236, 244)",
          drawBorder: false,
          borderDash: [2],
          zeroLineBorderDash: [2]
        }
      }],
    },
    legend: {
      display: false
    },
    tooltips: {
      titleMarginBottom: 10,
      titleFontColor: '#6e707e',
      titleFontSize: 15,
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      caretPadding: 10,
      callbacks: {
        label: function(tooltipItem, chart) {
          var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
          return datasetLabel + ': ' + number_format(tooltipItem.yLabel) + ' no ano.' ;
        }
      }
    },
  }

    });
	// myBarChart.data.datasets[0].data[0] = 11;
}

function finalizar_aguardar () {
	$('#modal_aguardar').modal('hide');
	atualizar_constante();
}

function atualizar_constante () {
	setTimeout(runn, 60000);
}

function runn () {
	gmuds_andamento();
	atualizar_constante();
}

function detalhar_gmud (id) {
	modal_detalhes_gmud.innerHTML = `
		<div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="TituloModalCentralizado">Detalhes da GMUD</h5>
	        </div>
	        <div id="conteudo_modal"class="modal-body">
	          <img style="width:50%;margin-left:140px" src="img/aguarde3.gif">
	        </div>
	        <div class="modal-footer">
	          <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
	        </div>
      	</div>
      	`;
    $('#modal_aguardar').modal('show');
    buscar_gmud_especifica(id);
}

function buscar_gmud_especifica (id) {
	fetch(`/leituras/gmud_especifica/${id}`,{
		method:"GET"
	}).then(resposta => {
		if (resposta.ok) {
			resposta.json().then(resultado => {
				conteudo_modal.innerHTML = `
					ID: ${resultado[0].idGmud} <br>
					Prioridade: ${resultado[0].prioridade} <br>
					Motivo: ${resultado[0].motivo} <br>
					Categoria: ${resultado[0].categoria} <br>
					Data Conclusão: ${resultado[0].data_conclusao} <br>
					Equipamento: ${resultado[0].descrição} ${" "} ${resultado[0].relevancia}  ${" com IP "} ${resultado[0].ipMaquina}   <br>
				`
			})
		} else {
			console.log("Errooooo");
		}
	})
}

var labels = [];
var qtd_by_areas = [];

function mes_area_buscar () {
	
    fetch(`/leituras/mes_area/${sessionStorage.empresa}`,{
    	method: "GET"
    }).then(resposta => {
    	if (resposta.ok) {
    		resposta.json().then(resultado => {
    			for (c = 0;c < resultado.length;c++) {
    				labels.push(resultado[c].areas);
    				qtd_by_areas.push(resultado[c].qtd_by_area);
    			}
    			console.log(labels);
    			console.log(qtd_by_areas);
    		})
    	} else {
    		console.log("Deu errooo");
    	}
    	setTimeout(dados_mes_area, 3000);
    	setTimeout(finalizar_aguardar, 3000);
    })
}

function dados_mes_area () {
	var dados = {
    labels: labels,
    datasets: [{
      data: qtd_by_areas,
      backgroundColor: ['#006400','#32CD32','#7CFC00' ,'#2E8B57','#808000'],
      hoverBackgroundColor: "#C71585",
      hoverBorderColor: "rgba(234, 236, 244, 1)",
    }]}
    mes_area_plotar(dados);
}

function mes_area_plotar (dadoss) {
    var ctx = document.getElementById("myPieChart");
    window.grafico = new Chart (ctx, {
        type: 'doughnut',
        data: dadoss,
        
    // Parte Fixa de configuração do gráfico, não precisa alterar
        options: {
	    maintainAspectRatio: false,
	    tooltips: {
	      backgroundColor: "rgb(255,255,255)",
	      bodyFontColor: "#858796",
	      borderColor: '#dddfeb',
	      borderWidth: 1,
	      xPadding: 15,
	      yPadding: 15,
	      displayColors: false,
	      caretPadding: 10,
	    },
	    legend: {
	      display: false
	    },
	    cutoutPercentage: 80,
	  	},
	});
}

function detalhar_andamento () {
  modal_detalhes_gmud.innerHTML = `
    <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="TituloModalCentralizado">Detalhes da GMUD</h5>
          </div>
          <div id="conteudo_modal"class="modal-body">
            <img style="width:50%;margin-left:140px" src="img/aguarde3.gif">
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
          </div>
        </div>
        `;
    $('#modal_aguardar').modal('show');
    buscar_gmuds_andamento();
}

function buscar_gmuds_andamento () {
  fetch(`/leituras/gmuds_andamento/${sessionStorage.empresa}`,{
    method:"GET"
  }).then(resposta => {
    if (resposta.ok) {
      resposta.json().then(resultado => {
        conteudo_modal.innerHTML = ""; 
        for (c = 0; c < resultado.length;c++) {
          conteudo_modal.innerHTML += `
          GMUD ${resultado[c].idGmud} <br><br>

          Prioridade: ${resultado[c].prioridade} <br>
          Motivo: ${resultado[c].motivo} <br>
          Categoria: ${resultado[c].categoria} <br>
          Equipamento: ${resultado[c].descrição} <br><br>
        `
        }
      })
    } else {
      console.log("Errooooo");
    }
  })
}

function detalhar_agendadas () {
  modal_detalhes_gmud.innerHTML = `
    <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="TituloModalCentralizado">Detalhes da GMUD</h5>
          </div>
          <div id="conteudo_modal"class="modal-body">
            <img style="width:50%;margin-left:140px" src="img/aguarde3.gif">
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
          </div>
        </div>
        `;
    $('#modal_aguardar').modal('show');
    buscar_gmuds_agendadas();
}

function buscar_gmuds_agendadas () {
  fetch(`/leituras/gmuds_agendadas/${sessionStorage.empresa}`,{
    method:"GET"
  }).then(resposta => {
    if (resposta.ok) {
      resposta.json().then(resultado => {
        conteudo_modal.innerHTML = ""; 
        for (c = 0; c < resultado.length;c++) {
          conteudo_modal.innerHTML += `
          GMUD ${resultado[c].idGmud} <br><br>

          Prioridade: ${resultado[c].prioridade} <br>
          Motivo: ${resultado[c].motivo} <br>
          Categoria: ${resultado[c].categoria} <br>
          Equipamento: ${resultado[c].descrição} <br><br>
        `
        }
      })
    } else {
      console.log("Errooooo");
    }
  })
}

function detalhar_equipamentos () {
  modal_detalhes_gmud.innerHTML = `
    <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="TituloModalCentralizado">Detalhes da GMUD</h5>
          </div>
          <div id="conteudo_modal"class="modal-body">
            <img style="width:50%;margin-left:140px" src="img/aguarde3.gif">
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
          </div>
        </div>
        `;
    $('#modal_aguardar').modal('show');
    buscar_equipamentos();
}

function buscar_equipamentos () {
  fetch(`/leituras/equipamentos/${sessionStorage.empresa}`,{
    method:"GET"
  }).then(resposta => {
    if (resposta.ok) {
      resposta.json().then(resultado => {
        conteudo_modal.innerHTML = ""; 
        for (c = 0; c < resultado.length;c++) {
          conteudo_modal.innerHTML += `
          Equipamento ${resultado[c].idEquipamento} <br><br>

          Relevância: ${resultado[c].relevancia} <br>
          IP: ${resultado[c].ipMaquina} <br>
          Descrição: ${resultado[c].descrição} <br><br>
        `
        }
      })
    } else {
      console.log("Errooooo");
    }
  })
}


function detalhar_concluidas () {
  modal_detalhes_gmud.innerHTML = `
    <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="TituloModalCentralizado">Detalhes da GMUD</h5>
          </div>
          <div id="conteudo_modal"class="modal-body">
            <img style="width:50%;margin-left:140px" src="img/aguarde3.gif">
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
          </div>
        </div>
        `;
    $('#modal_aguardar').modal('show');
    buscar_concluidas();
}

function buscar_concluidas () {
  fetch(`/leituras/b_concluidas/${sessionStorage.empresa}`,{
    method:"GET"
  }).then(resposta => {
    if (resposta.ok) {
      resposta.json().then(resultado => {
        conteudo_modal.innerHTML = ""; 
        for (c = 0; c < resultado.length;c++) {
          conteudo_modal.innerHTML += `
          GMUD ${resultado[c].idGmud} <br><br>

          Prioridade: ${resultado[c].prioridade} <br>
          Motivo: ${resultado[c].motivo} <br>
          Categoria: ${resultado[c].categoria} <br>
          Equipamento: ${resultado[c].descrição} <br><br>
        `
        }
      })
    } else {
      console.log("Errooooo");
    }
  })
}