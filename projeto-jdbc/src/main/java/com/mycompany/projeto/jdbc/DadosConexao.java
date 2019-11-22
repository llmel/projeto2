/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.projeto.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author Leonardo
 */
public class DadosConexao {

    private BasicDataSource dataSource;

    public DadosConexao() {

        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://grupo5projeto.database.windows.net:1433;database=BancoProjeto;user=bandtec@grupo5projeto;password={#Gf43988602825};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
        // quem for acessar do yoshi - > localhost -> 10.3.2.180
        dataSource.setUsername("bandtec");
        dataSource.setPassword("#Gf43988602825");
    }

    public BasicDataSource getDataSource() {
        return dataSource;
    }

}
