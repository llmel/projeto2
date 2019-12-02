-- GMUDS EM ANDAMENTO
select count (fkstatus) as gmuds_andamento
from gmud g
join equipamento e on g.fkequipamento = e.idequipamento
where g.fkstatus = 2
and e.fkempresa = 10000

-- AREAS AFETADAS PELAS GMUDS EM ANDAMENTO
select count(distinct a.fkareaafetada) as areas_afetadas
from gmud g
join equipamento e on g.fkequipamento = e.idequipamento
join afeta a on e.fkempresa = a.fkempresaafeta
where g.fkstatus = 2
and e.fkempresa = 10000 

-- EQUIPAMENTOS PARADOS PELAS GMUDS EM ANDAMENTO
select count(e.idequipamento) as equip_parados
from afeta a
join equipamento e on a.fkareaafeta = e.fkareas
where e.fkareas in (
    select distinct a.fkareaafetada
    from equipamento e
    join gmud g on g.fkequipamento = e.idequipamento
    join afeta a on a.fkareaafeta = e.fkareas
    where g.fkstatus = 2
    and e.fkempresa = 10000
)
and e.fkempresa in (
    select distinct a.fkempresaafetada
    from equipamento e
    join gmud g on g.fkequipamento = e.idequipamento
    join afeta a on a.fkareaafeta = e.fkareas
    where g.fkstatus = 2
    and e.fkempresa = 10000
)

-- GMUDS CONCLUIDAS NO DIA
select count(fkstatus) as gmuds_concluidas
from gmud g
join equipamento e on g.fkequipamento = e.idequipamento
where g.fkstatus = 3
and e.fkempresa = 10000
and MONTH(datahora) = MONTH(GETDATE())
and DAY(datahora) = DAY(GETDATE())

-- GMUDS CONCLUIDAS NOS ÚLTIMOS 7 DIAS
select g.idgmud as id,a.nomearea as area_afetada,g.motivo,g.categoria as categoria_gmud,FORMAT(datahora,'dd/MM/yyyy') as data_conclusao
from gmud g
join equipamento e on g.fkequipamento = e.idequipamento
join areas a on e.fkareas = a.idareas
where g.fkstatus = 3
and e.fkempresa = 10000
and datahora >= GETDATE()-7 

-- GMUDS REALIZADAS MES A MES
select MONTH(datahora) as meses,count(MONTH(datahora)) as qtd_by_month
from gmud g
join equipamento e on e.idequipamento = g.fkequipamento
where g.fkstatus = 3
and e.fkempresa = 10000
and YEAR(datahora) = YEAR(GETDATE())
group by MONTH(datahora)

-- GMUDS POR AREA NO ÚLTIMO MÊS
select a.nomearea as areas,count(fkareas) as qtd_by_area
from gmud g
join equipamento e on e.idequipamento = g.fkequipamento
join areas a on e.fkareas = a.idareas
where MONTH(g.datahora) = MONTH(GETDATE())-1
and e.fkempresa = 10000
group by a.nomearea

-- BUSCAR GMUD ESPECIFICA
select *,FORMAT(g.datahora,'dd/MM/yyyy') as data_conclusao
from gmud g
join equipamento e on g.fkequipamento = e.idequipamento
where idgmud = 500


