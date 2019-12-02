// não mexa nestas 3 linhas!
var express = require('express');
var router = express.Router();
var banco = require('../app-banco');
// não mexa nessas 3 linhas!

// GMUDS EM ANDAMENTO
router.get("/andamento/:empresa",function (requisicao,resposta) {
    console.log("Chegou no endpoint de andamento");
    banco.conectar().then(() => {
      return banco.sql.query(`
          select count (fkstatus) as gmuds_andamento
          from gmud g
          join equipamento e on g.fkequipamento = e.idequipamento
          where g.fkstatus = 2
          and e.fkempresa = ${requisicao.params.empresa}
        `).then(consulta => {
          console.log(consulta);
          resposta.send(consulta.recordset[0]);
        }).finally(() => {
          banco.sql.close();
        })
    })
})

// AREAS AFETADAS PELAS GMUDS EM ANDAMENTO
router.get("/a_afetadas/:empresa",function (requisicao,resposta) {
    console.log("Chegou no endpoint de afetadas");
    banco.conectar().then(() => {
      return banco.sql.query(`
          select count(distinct a.fkareaafetada) as areas_afetadas
          from gmud g
          join equipamento e on g.fkequipamento = e.idequipamento
          join afeta a on e.fkempresa = a.fkempresaafeta
          where g.fkstatus = 2
          and e.fkempresa = ${requisicao.params.empresa}
        `).then(consulta => {
          console.log(consulta);
          resposta.send(consulta.recordset[0]);
        }).finally(() => {
          banco.sql.close();
        })
    })
})

// EQUIPAMENTOS AFETADOS PELAS GMUDS EM ANDAMENTO
router.get("/e_afetados/:empresa",function (requisicao,resposta) {
    console.log("Chegou no endpoint de parados");
    banco.conectar().then(() => {
      return banco.sql.query(`
          select count(e.idequipamento) as equip_parados
          from afeta a
          join equipamento e on a.fkareaafeta = e.fkareas
          where e.fkareas in (
              select distinct a.fkareaafetada
              from equipamento e
              join gmud g on g.fkequipamento = e.idequipamento
              join afeta a on a.fkareaafeta = e.fkareas
              where g.fkstatus = 2
              and e.fkempresa = ${requisicao.params.empresa}
          )
          and e.fkempresa in (
              select distinct a.fkempresaafetada
              from equipamento e
              join gmud g on g.fkequipamento = e.idequipamento
              join afeta a on a.fkareaafeta = e.fkareas
              where g.fkstatus = 2
              and e.fkempresa = ${requisicao.params.empresa}
          )
        `).then(consulta => {
          console.log(consulta);
          resposta.send(consulta.recordset[0]);
        }).finally(() => {
          banco.sql.close();
        })
    })
})

// GMUDS CONCLUIDAS NO DIA ATUAL
router.get("/concluidas/:empresa",function (requisicao,resposta) {
    console.log("Chegou no endpoint de concluidas");
    banco.conectar().then(() => {
      return banco.sql.query(`
          select count(fkstatus) as gmuds_concluidas
          from gmud g
          join equipamento e on g.fkequipamento = e.idequipamento
          where g.fkstatus = 3
          and e.fkempresa = ${requisicao.params.empresa}
          and MONTH(datahora) = MONTH(GETDATE())
          and DAY(datahora) = DAY(GETDATE())
        `).then(consulta => {
          console.log(consulta);
          resposta.send(consulta.recordset[0]);
        }).finally(() => {
          banco.sql.close();
        })
    })
})

// GMUDS CONCLUIDAS NOS ÚLTIMOS 7 DIAS
router.get("/sete_dias/:empresa",(requisicao,resposta) => {
  console.log("Chegou no endpoint de sete dias");
  banco.conectar().then(() => {
    return banco.sql.query(`
      select g.idgmud as id,a.nomearea as area_afetada,g.motivo,g.categoria as categoria_gmud,FORMAT(datahora,'dd/MM/yyyy') as data_conclusao
      from gmud g
      join equipamento e on g.fkequipamento = e.idequipamento
      join areas a on e.fkareas = a.idareas
      where g.fkstatus = 3
      and e.fkempresa = ${requisicao.params.empresa}
      and datahora >= GETDATE()-7 
      `).then(resultado => {
        console.log(resultado);
        resposta.send(resultado.recordset);
      }).finally(() => {
        banco.sql.close();
      })
  })
})

// GMUDS REALIZADAS MES A MES
router.get("/mes_a_mes/:empresa",(requisicao,resposta) => {
  console.log("Chegou no endpoint de mes_a_mes");
  banco.conectar().then(() => {
    return banco.sql.query(`
        select MONTH(datahora) as meses,count(MONTH(datahora)) as qtd_by_month
        from gmud g
        join equipamento e on e.idequipamento = g.fkequipamento
        where g.fkstatus = 3
        and e.fkempresa = ${requisicao.params.empresa}
        and YEAR(datahora) = YEAR(GETDATE())
        group by MONTH(datahora)
      `).then(resultado => {
        console.log(resultado);
        resposta.send(resultado.recordset);
      }).finally(() => {
        banco.sql.close();
      })
  })
})

// GMUDS POR AREA NO ULTIMO MES
router.get("/mes_area/:empresa",(requisicao,resposta) => {
  console.log("Chegou no endpoint de mes_area");
  banco.conectar().then(() => {
    return banco.sql.query(`
        select a.nomearea as areas,count(fkareas) as qtd_by_area
        from gmud g
        join equipamento e on e.idequipamento = g.fkequipamento
        join areas a on e.fkareas = a.idareas
        where MONTH(g.datahora) = MONTH(GETDATE())-1
        and e.fkempresa = ${requisicao.params.empresa}
        group by a.nomearea
      `).then(resultado => {
        console.log(resultado);
        resposta.send(resultado.recordset);
      }).finally(() => {
        banco.sql.close();
      })
  })
})

// BUSCAR DETALHES DE GMUD ESPECÍFICA
router.get(`/gmud_especifica/:id`,(requisicao,resposta) => {
  console.log("Chegou no endpoint de gmud gmud_especifica");
  banco.conectar().then(() => {
    return banco.sql.query(`
        select *,FORMAT(g.datahora,'dd/MM/yyyy') as data_conclusao
        from gmud g
        join equipamento e on g.fkequipamento = e.idequipamento
        where idgmud = ${requisicao.params.id}
      `).then(resultado => {
        console.log(resultado);
        resposta.send(resultado.recordset);
      }).finally(() => {
        banco.sql.close();
      })
  })
})

// QUAIS GMUDS EM ANDAMENTO




// não mexa nesta linha!
module.exports = router;
