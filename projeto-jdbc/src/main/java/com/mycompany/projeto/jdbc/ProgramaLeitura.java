/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projeto.jdbc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Leonardo
 */
public class ProgramaLeitura {

    public static void main(String[] args) {
        DadosConexao dadosConexao = new DadosConexao();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dadosConexao.getDataSource());

        List lista = jdbcTemplate.queryForList("select * from leitura");

        System.out.println("Consulta" + lista);

        List listaCpu = jdbcTemplate.queryForList(
                "select * from leitura WHERE origem = ?", "CPU");

        System.out.println("consulta CPU: " + listaCpu);

        System.out.println("Consulta" + lista);

        List listaCpuAlta = jdbcTemplate.queryForList(
                "select * from leitura WHERE "
                + " origem = ? and valor > ?", "CPU", 99);

        System.out.println("consulta CPU: " + listaCpuAlta);

        List listaCpuPassado;
        listaCpuPassado = jdbcTemplate.queryForList(
                "select * from leitura WHERE "
                + " origem = ? and data_hora < ?", "CPU", LocalDateTime.now());

        System.out.println("consulta CPU: " + listaCpuPassado);

        // INSERT 
        jdbcTemplate.update(
                "insert into leitura (origem, valor, data_hora)"
                + " values (?,?,?)",
                "Rede", 33.5, LocalDateTime.now());
        
        // UPDATE
        
        jdbcTemplate.update(
                "update leitura set valor = ? "
                        + " where origem = ?",
                50, "Disco");
        
        // DELETE
        jdbcTemplate.update(
                "delete from leitura where valor <= ?", 98);
        
        //exercicio 1
       
        // faça um select no worckbench p/ confirmar
        
        // CRIE UM CODIGO QUE:
        // a) INSIRA 3 REGISTROS PARA A ORIGEM 'CPU'
        // B) ESSES VALORES DEVEM SER ALEATÓRIO ENTRE 0 E 100
        // C) O DATA_HORA DEVE SER SEMPRE A DATA E HORA ATUAL
        
            Random sorteador = new Random();
            
            for (int r=0; r<3; r++){
                Double numero = sorteador.nextInt(100) + 1.0;
                String sql = "insert into leitura (origem, valor, data_hora)"
                + " values (?,?,?)";
                jdbcTemplate.update(sql, "CPU", numero, LocalDateTime.now());
                
            } }
    
    
    

}
