const express = require('express');
const router = express.Router();
const banco = require('../app-banco');

// Consulta de Relatórios
router.post("/relatorios", (requisicao,resposta) => {
	console.log("Chegou no end point");
	console.log(requisicao.body);
	banco.conectar().then(() => {
    return banco.sql.query(`
        select FORMAT(data,'dd/MM/yyyy') as data,idgmud,motivo,categoria from testerelatorio where data >= '${requisicao.body.dtInicio}' and data <= '${requisicao.body.dtFim}' order by convert(datetime, data, 103)
	        `);
		}).then(consulta => {
	    	console.log(consulta);
	        resposta.send(consulta.recordset);
		}).finally(() => {
	    		banco.sql.close();
	  	});
  
})

// Consulta de Agendadas Com Base no Mês e Dia Atual
router.post("/agendadas", (requisicao,resposta) => {
	console.log("Chegou no end-p");
	console.log(requisicao.body);
	banco.conectar().then(() => {
		return banco.sql.query(`
			select FORMAT(data,'dd') as date,idgmud as gmud,motivo from testerelatorio where DAY(data) >= ${requisicao.body.dia} and MONTH(data) = ${requisicao.body.mes} and YEAR(data) = ${requisicao.body.ano}
			`);
	}).then(consulta => {
		console.log(consulta);
		resposta.send(consulta.recordset);
	}).finally(() => {
		banco.sql.close();
	});
})

// Consulta de Áreas
router.post("/consultar_areas", (requisicao,resposta) => {
	console.log("Chegou no end-pA");
	console.log(requisicao.body);
	banco.conectar().then(() => {
		return banco.sql.query(`
			select a.idareas,a.nomearea,e.nomeEmpresa,a.fkempresa as idempresa
			from areas a
			join empresa e on e.idempresa = a.fkempresa
			where a.fkempresa = '${requisicao.body.empresa}'
		`).then(function (consulta) {
			resposta.send(consulta.recordset);
		})
	}).catch(err => {
		console.log(err);
	}).finally(() => { 
		banco.sql.close();
	});
})

// Inserir Áreas
router.post("/inserir_areas", (requisicao,resposta) => {
	console.log("Chegou no end-pA");
	console.log(requisicao.body);
	banco.conectar().then(() => {
		return banco.sql.query(`
			insert into areas values (${requisicao.body.area},'${requisicao.body.nome}',${requisicao.body.empresa})
		`).then(function () {
			resposta.sendStatus(201);
		})
	}).catch(err => {
		console.log(err);
		var erro = `Erro no cadastro: ${err}`;
		resposta.status(500).send(erro);
	}).finally(() => { 
		banco.sql.close();
	});
})

// Combo dos nomes das empresas
router.post("/combo_empresas", (requisicao,resposta) => {
	console.log("Chegou no end-pA");
	banco.conectar().then(() => {
		return banco.sql.query(`
			select nomeempresa,idempresa from empresa
		`).then(function (consulta) {
			resposta.send(consulta.recordset);
		})
	}).catch(err => {
		console.log(err);
	}).finally(() => { 
		banco.sql.close();
	});
})

// Consultar funcionários
router.post("/consultar_funcionarios", (requisicao,resposta) => {
	console.log("Chegou no end-pB");
	console.log(requisicao.body);
	banco.conectar().then(() => {
		return banco.sql.query(`
			select idfunc as id, logins as login, nome, cargo from funcionario where fkempresa = ${requisicao.body.empresa}
		`).then(function (consulta) {
			resposta.send(consulta.recordset);
		})
	}).catch(err => {
		console.log(err);
	}).finally(() => { 
		banco.sql.close();
	});
})

// Inserir funcionários
router.post("/inserir_funcionarios", (requisicao,resposta) => {
	console.log("Chegou no end-pB");
	console.log(requisicao.body);
	banco.conectar().then(() => {
		return banco.sql.query(`
			insert into funcionario values (${requisicao.body.id},'${requisicao.body.senha}','${requisicao.body.login}','${requisicao.body.nome}','${requisicao.body.cargo}',${requisicao.body.empresa})
		`).then(function () {
			resposta.sendStatus(201);
		})
	}).catch(err => {
		console.log(err);
		var erro = `Erro no cadastro: ${err}`;
		resposta.status(500).send(erro);
	}).finally(() => { 
		banco.sql.close();
	});
})

// Consultar equipamentos
router.post("/consultar_equipamentos", (requisicao,resposta) => {
	console.log("Chegou no end-pC");
	console.log(requisicao.body);
	banco.conectar().then(() => {
		return banco.sql.query(`
			select idequipamento as id,descrição as descricao,ipmaquina as ip,relevancia from equipamento where fkempresa = ${requisicao.body.empresa}
		`).then(function (consulta) {
			resposta.send(consulta.recordset);
		})
	}).catch(err => {
		console.log(err);
	}).finally(() => { 
		banco.sql.close();
	});
})


// Inserir equipamentos
router.post("/inserir_equipamentos", (requisicao,resposta) => {
	console.log("Chegou no end-pC");
	console.log(requisicao.body);
	banco.conectar().then(() => {
		return banco.sql.query(`
			insert into equipamento values (${requisicao.body.id},'${requisicao.body.relevancia}','${requisicao.body.ip}','${requisicao.body.descricao}',${requisicao.body.area},${requisicao.body.empresa})
		`).then(function () {
			resposta.sendStatus(201);
		})
	}).catch(err => {
		console.log(err);
		var erro = `Erro no cadastro: ${err}`;
		resposta.status(500).send(erro);
	}).finally(() => { 
		banco.sql.close();
	});
})

// Modal com detalhes de uma GMUD
router.post('/detalhes_gmud',(requisicao,resposta) => {
	console.log("Chegou no end-pD");
	console.log(requisicao.body);
	banco.conectar().then(() => {
		return banco.sql.query(`
			select FORMAT(data,'dd') as date,idgmud as gmud,motivo from testerelatorio where DAY(data) >= ${requisicao.body.dia} and MONTH(data) = ${requisicao.body.mes} and YEAR(data) = ${requisicao.body.ano}
		`).then(function (consulta) {
			resposta.send(consulta.recordset);
		})
	}).catch(err => {
		console.log(err);
	}).finally(() => {
		banco.sql.close();
	})
})

module.exports = router;