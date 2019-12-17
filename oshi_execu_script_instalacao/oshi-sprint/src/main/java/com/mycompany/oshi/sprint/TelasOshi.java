/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.oshi.sprint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author artoliveira
 */
public class TelasOshi extends javax.swing.JFrame {

    /**
     * Creates new form TelasOshi
     */
    public TelasOshi() {
        initComponents();
        pbCpu.setStringPainted(true);
        pbMemoria.setStringPainted(true);
    }

    public void mudarTela(JPanel tela) {
        jLayeredPane1.removeAll();
        jLayeredPane1.add(tela);
        jLayeredPane1.repaint();
        jLayeredPane1.revalidate();
    }

    Boolean logado = false;

    Oshi info;

    public Boolean validacaoLogin() {
        if (logado) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Realize o login primeiro");
            return false;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Dados Banco Azure">
    String hostName = "srvgrupo3b.database.windows.net"; // update me
    String dbName = "bdgrupo3b"; // update me
    String user = "usergrupo3b"; // update me
    String password = "#Gfgrupo3b"; // update me
    String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
            + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
    Connection connection = null;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Dados Slack">
    String urlSlack = "https://hooks.slack.com/services/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";
    private static HttpURLConnection con;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Dados gmud">
    String idEncontrado = "";
    String categoriaGmud = "";
    String motivoGmud = "";
    //</editor-fold>

    public String buscarGmud(Integer gmud) {

        idEncontrado = "";
        categoriaGmud = "";
        motivoGmud = "";

        try {
            connection = DriverManager.getConnection(url);
            String schema = connection.getSchema();

            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT idGmud, categoria, motivo "
                    + "FROM [dbo].[gmud] "
                    + "WHERE idGmud = " + gmud;

            try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(selectSql)) {

                // Print results from select statement
                while (resultSet.next()) {
                    idEncontrado = resultSet.getString(1);
                    categoriaGmud = resultSet.getString(2);
                    motivoGmud = resultSet.getString(3);
                }
                connection.close();

                if (idEncontrado.equals("")) {
                    return ("ID " + gmud + " não encontrado");
                } else {
                    return ("idGMUD " + idEncontrado + " de categoria: "
                            + categoriaGmud);
                }
            }

        } catch (Exception e) {
            return ("Erro na conexão com o banco. Erro: " + e);
        }
    }

    public static void gerarTXT(String argumento) throws IOException {
        String path = new File(".").getCanonicalPath() + "\\LOG.txt";

        try (BufferedWriter bf = new BufferedWriter(new FileWriter(path, true))) {
            bf.append(argumento);
            bf.newLine();
        }
    }
    
    public static void gerarLOG(String argumento) throws IOException {
        String path = new File(".").getCanonicalPath() + "\\relatorio.txt";

        try (BufferedWriter bf = new BufferedWriter(new FileWriter(path, true))) {
            bf.append(argumento);
            bf.newLine();
        }
    }

    public void mensagemSlack() throws IOException {
        try {

            URL myurl = new URL(urlSlack);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            JSONObject mensagem = new JSONObject();

            mensagem.put("text", "A Gmud " + idEncontrado + " que esta ocorrendo, "
                    + "pois " + motivoGmud + " foi iniciada");
//            if (frase <= 20) {
//                mensagem.put("text", "O meu texto não funciona");
//            } else if (frase >= 75) {
//                mensagem.put("text", "IIIHHHH VAI DAR RUIM, mais de 75% do seu disco esta sendo usado!");
//            } else {
//                mensagem.put("text", "Cuidado, 50% do seu disco esta sendo usado!");
//            }

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {

                wr.writeBytes(mensagem.toString());
            }

            StringBuilder content;

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            //System.out.println(content.toString());
            if ("ok\r\n".equals(content.toString())) {
                taLog.append("Gmud iniciada \n");
            };

        } finally {

            con.disconnect();
        }
    }

    
    public void mensagemSlackFinalizado() throws IOException {
        try {

            URL myurl = new URL(urlSlack);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            JSONObject mensagem = new JSONObject();

            mensagem.put("text", "A Gmud " + idEncontrado + " foi finalizada com sucesso. O " +motivoGmud+ " foi corrigido");
//            if (frase <= 20) {
//                mensagem.put("text", "O meu texto não funciona");
//            } else if (frase >= 75) {
//                mensagem.put("text", "IIIHHHH VAI DAR RUIM, mais de 75% do seu disco esta sendo usado!");
//            } else {
//                mensagem.put("text", "Cuidado, 50% do seu disco esta sendo usado!");
//            }

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {

                wr.writeBytes(mensagem.toString());
            }

            StringBuilder content;

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            //System.out.println(content.toString());
            if ("ok\r\n".equals(content.toString())) {
                taLog.append("Gmud finalizada");
            };

        } finally {

            con.disconnect();
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jlInicial = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jlLogin = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lbLogin = new javax.swing.JTextField();
        pfSenha = new javax.swing.JPasswordField();
        jButton4 = new javax.swing.JButton();
        jlHome = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jlRecursos = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        pbCpu = new javax.swing.JProgressBar();
        jLabel6 = new javax.swing.JLabel();
        pbMemoria = new javax.swing.JProgressBar();
        lbNumeroProcess = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        lbIdProces = new javax.swing.JLabel();
        lbIdProces1 = new javax.swing.JLabel();
        lbIdProces2 = new javax.swing.JLabel();
        lbIdProces3 = new javax.swing.JLabel();
        lbIdProces4 = new javax.swing.JLabel();
        lbIdProces5 = new javax.swing.JLabel();
        lbIdProces6 = new javax.swing.JLabel();
        lbIdProces7 = new javax.swing.JLabel();
        lbIdProces8 = new javax.swing.JLabel();
        lbIdProces9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lbCpuProces = new javax.swing.JLabel();
        lbCpuProces1 = new javax.swing.JLabel();
        lbCpuProces2 = new javax.swing.JLabel();
        lbCpuProces3 = new javax.swing.JLabel();
        lbCpuProces4 = new javax.swing.JLabel();
        lbCpuProces5 = new javax.swing.JLabel();
        lbCpuProces6 = new javax.swing.JLabel();
        lbCpuProces7 = new javax.swing.JLabel();
        lbCpuProces8 = new javax.swing.JLabel();
        lbCpuProces9 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lbMemoProces = new javax.swing.JLabel();
        lbMemoProces1 = new javax.swing.JLabel();
        lbMemoProces2 = new javax.swing.JLabel();
        lbMemoProces3 = new javax.swing.JLabel();
        lbMemoProces4 = new javax.swing.JLabel();
        lbMemoProces5 = new javax.swing.JLabel();
        lbMemoProces6 = new javax.swing.JLabel();
        lbMemoProces7 = new javax.swing.JLabel();
        lbMemoProces8 = new javax.swing.JLabel();
        lbMemoProces9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbVszProces = new javax.swing.JLabel();
        lbVszProces1 = new javax.swing.JLabel();
        lbVszProces2 = new javax.swing.JLabel();
        lbVszProces3 = new javax.swing.JLabel();
        lbVszProces4 = new javax.swing.JLabel();
        lbVszProces5 = new javax.swing.JLabel();
        lbVszProces6 = new javax.swing.JLabel();
        lbVszProces7 = new javax.swing.JLabel();
        lbVszProces8 = new javax.swing.JLabel();
        lbVszProces9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lbRssProces = new javax.swing.JLabel();
        lbRssProces1 = new javax.swing.JLabel();
        lbRssProces2 = new javax.swing.JLabel();
        lbRssProces3 = new javax.swing.JLabel();
        lbRssProces4 = new javax.swing.JLabel();
        lbRssProces5 = new javax.swing.JLabel();
        lbRssProces6 = new javax.swing.JLabel();
        lbRssProces7 = new javax.swing.JLabel();
        lbRssProces8 = new javax.swing.JLabel();
        lbRssProces9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lbNomeProces = new javax.swing.JLabel();
        lbNomeProces1 = new javax.swing.JLabel();
        lbNomeProces2 = new javax.swing.JLabel();
        lbNomeProces3 = new javax.swing.JLabel();
        lbNomeProces4 = new javax.swing.JLabel();
        lbNomeProces5 = new javax.swing.JLabel();
        lbNomeProces6 = new javax.swing.JLabel();
        lbNomeProces7 = new javax.swing.JLabel();
        lbNomeProces8 = new javax.swing.JLabel();
        lbNomeProces9 = new javax.swing.JLabel();
        jlGmud = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        tfIdGmud = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        taLog = new javax.swing.JTextArea();
        lbMensagem = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLayeredPane1.setLayout(new java.awt.CardLayout());

        jButton1.setFont(new java.awt.Font("Ebrima", 1, 11)); // NOI18N
        jButton1.setText("FAZER LOGIN");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButton2.setText("CADASTRAR-SE");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("THE CHANGES MANAGER ");
        jLabel1.setBorder(new javax.swing.border.MatteBorder(null));

        javax.swing.GroupLayout jlInicialLayout = new javax.swing.GroupLayout(jlInicial);
        jlInicial.setLayout(jlInicialLayout);
        jlInicialLayout.setHorizontalGroup(
            jlInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jlInicialLayout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addGroup(jlInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jlInicialLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jlInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(163, Short.MAX_VALUE))
        );
        jlInicialLayout.setVerticalGroup(
            jlInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jlInicialLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(125, Short.MAX_VALUE))
        );

        jLayeredPane1.add(jlInicial, "card2");

        jLabel3.setFont(new java.awt.Font("Ebrima", 0, 11)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Senha:");

        jButton3.setText("Confirmar");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Century Schoolbook", 1, 11)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Login:");
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Ebrima", 0, 11)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Usuário:");

        jButton4.setText("VOLTAR");
        jButton4.setBorder(null);
        jButton4.setBorderPainted(false);
        jButton4.setFocusable(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jlLoginLayout = new javax.swing.GroupLayout(jlLogin);
        jlLogin.setLayout(jlLoginLayout);
        jlLoginLayout.setHorizontalGroup(
            jlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jlLoginLayout.createSequentialGroup()
                .addGroup(jlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jlLoginLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jlLoginLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jlLoginLayout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jlLoginLayout.createSequentialGroup()
                                .addGroup(jlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(jlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lbLogin)
                                    .addComponent(pfSenha, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)))))
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(134, Short.MAX_VALUE))
        );
        jlLoginLayout.setVerticalGroup(
            jlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jlLoginLayout.createSequentialGroup()
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(jlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pfSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );

        jLayeredPane1.add(jlLogin, "card3");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText(" Bem vindo(a) à ferramenta de monitoramento de mudanças do \nTCM.\n\n Aqui você pode monitorar os recurso da maquina que \nsofrerá a mudança, além de poder registrar essas \ninformações durante a GMUD e gerar um relatório com os \ndados obtidos.\n\n Em caso de duvidas, problemas ou sugestões entre em contato \nconosco através do nosso site.");
        jTextArea1.setFocusable(false);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jlHomeLayout = new javax.swing.GroupLayout(jlHome);
        jlHome.setLayout(jlHomeLayout);
        jlHomeLayout.setHorizontalGroup(
            jlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jlHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                .addContainerGap())
        );
        jlHomeLayout.setVerticalGroup(
            jlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jlHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLayeredPane1.add(jlHome, "card4");

        jlRecursos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("CPU:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Memória:");

        jButton6.setText("ATUALIZAR");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel7.setText("IDProcesso");

        lbIdProces.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbIdProces1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbIdProces2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbIdProces3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbIdProces4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbIdProces5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbIdProces6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbIdProces7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbIdProces8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbIdProces9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("%CPU");

        lbCpuProces.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbCpuProces1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbCpuProces2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbCpuProces3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbCpuProces4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbCpuProces5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbCpuProces6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbCpuProces7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbCpuProces8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbCpuProces9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel9.setText("%Memória");

        lbMemoProces.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbMemoProces1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbMemoProces2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbMemoProces3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbMemoProces4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbMemoProces5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbMemoProces6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbMemoProces7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbMemoProces8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbMemoProces9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("VSZ");

        lbVszProces.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbVszProces1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbVszProces2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbVszProces3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbVszProces4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbVszProces5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbVszProces6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbVszProces7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbVszProces8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbVszProces9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("RSS");

        lbRssProces.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbRssProces1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbRssProces2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbRssProces3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbRssProces4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbRssProces5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbRssProces6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbRssProces7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbRssProces8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbRssProces9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Nome");

        lbNomeProces.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbNomeProces1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbNomeProces2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbNomeProces3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbNomeProces4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbNomeProces5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbNomeProces6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbNomeProces7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbNomeProces8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lbNomeProces9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jlRecursosLayout = new javax.swing.GroupLayout(jlRecursos);
        jlRecursos.setLayout(jlRecursosLayout);
        jlRecursosLayout.setHorizontalGroup(
            jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jlRecursosLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton6))
            .addGroup(jlRecursosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbNumeroProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jlRecursosLayout.createSequentialGroup()
                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pbMemoria, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                            .addComponent(pbCpu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jlRecursosLayout.createSequentialGroup()
                        .addComponent(lbIdProces2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbCpuProces2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbMemoProces2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbVszProces2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbRssProces2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbNomeProces2, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                    .addGroup(jlRecursosLayout.createSequentialGroup()
                        .addComponent(lbIdProces3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbCpuProces3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbMemoProces3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbVszProces3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbRssProces3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbNomeProces3, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                    .addGroup(jlRecursosLayout.createSequentialGroup()
                        .addComponent(lbIdProces4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbCpuProces5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbMemoProces4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbVszProces4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbRssProces4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbNomeProces4, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                    .addGroup(jlRecursosLayout.createSequentialGroup()
                        .addComponent(lbIdProces5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbCpuProces4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbMemoProces5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbVszProces5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbRssProces5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbNomeProces5, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                    .addGroup(jlRecursosLayout.createSequentialGroup()
                        .addComponent(lbIdProces6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbCpuProces6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbMemoProces6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbVszProces6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbRssProces6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbNomeProces6, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                    .addGroup(jlRecursosLayout.createSequentialGroup()
                        .addComponent(lbIdProces7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbCpuProces7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbMemoProces7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbVszProces7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbRssProces7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbNomeProces7, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                    .addGroup(jlRecursosLayout.createSequentialGroup()
                        .addComponent(lbIdProces8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbCpuProces8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbMemoProces8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbVszProces8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbRssProces8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbNomeProces8, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                    .addGroup(jlRecursosLayout.createSequentialGroup()
                        .addComponent(lbIdProces9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbCpuProces9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbMemoProces9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbVszProces9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbRssProces9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbNomeProces9, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                    .addGroup(jlRecursosLayout.createSequentialGroup()
                        .addComponent(lbIdProces1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbCpuProces1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbMemoProces1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbVszProces1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbRssProces1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbNomeProces1, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                    .addGroup(jlRecursosLayout.createSequentialGroup()
                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                            .addComponent(lbIdProces, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                        .addGap(8, 8, 8)
                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                            .addComponent(lbCpuProces, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                            .addComponent(lbMemoProces, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jlRecursosLayout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                            .addGroup(jlRecursosLayout.createSequentialGroup()
                                .addComponent(lbVszProces, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbRssProces, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbNomeProces, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jlRecursosLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel10, jLabel11, jLabel12, jLabel7, jLabel8, jLabel9, lbCpuProces, lbCpuProces1, lbCpuProces2, lbCpuProces3, lbCpuProces4, lbCpuProces5, lbCpuProces6, lbCpuProces7, lbCpuProces8, lbCpuProces9, lbIdProces, lbIdProces1, lbIdProces2, lbIdProces3, lbIdProces4, lbIdProces5, lbIdProces6, lbIdProces7, lbIdProces8, lbIdProces9, lbMemoProces, lbMemoProces1, lbMemoProces2, lbMemoProces3, lbMemoProces4, lbMemoProces5, lbMemoProces6, lbMemoProces7, lbMemoProces8, lbMemoProces9, lbNomeProces, lbNomeProces1, lbNomeProces2, lbNomeProces3, lbNomeProces4, lbNomeProces5, lbNomeProces6, lbNomeProces7, lbNomeProces8, lbNomeProces9, lbRssProces, lbRssProces1, lbRssProces2, lbRssProces3, lbRssProces4, lbRssProces5, lbRssProces6, lbRssProces7, lbRssProces8, lbRssProces9, lbVszProces, lbVszProces1, lbVszProces2, lbVszProces3, lbVszProces4, lbVszProces5, lbVszProces6, lbVszProces7, lbVszProces8, lbVszProces9});

        jlRecursosLayout.setVerticalGroup(
            jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jlRecursosLayout.createSequentialGroup()
                .addComponent(jButton6)
                .addGap(16, 16, 16)
                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(pbCpu, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jlRecursosLayout.createSequentialGroup()
                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jlRecursosLayout.createSequentialGroup()
                                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jlRecursosLayout.createSequentialGroup()
                                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jlRecursosLayout.createSequentialGroup()
                                                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jlRecursosLayout.createSequentialGroup()
                                                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(jlRecursosLayout.createSequentialGroup()
                                                                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                    .addGroup(jlRecursosLayout.createSequentialGroup()
                                                                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                            .addGroup(jlRecursosLayout.createSequentialGroup()
                                                                                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                    .addComponent(pbMemoria, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(lbNumeroProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(2, 2, 2)
                                                                                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                    .addComponent(jLabel7)
                                                                                    .addComponent(jLabel8)
                                                                                    .addComponent(jLabel9)
                                                                                    .addComponent(jLabel10)
                                                                                    .addComponent(jLabel11)
                                                                                    .addComponent(jLabel12))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                    .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(lbRssProces, javax.swing.GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE)
                                                                                        .addComponent(lbVszProces, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(lbCpuProces, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(lbIdProces, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(lbMemoProces, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                                    .addComponent(lbNomeProces, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                    .addComponent(lbVszProces1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                    .addGroup(jlRecursosLayout.createSequentialGroup()
                                                                                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                            .addComponent(lbIdProces1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                            .addComponent(lbMemoProces1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                                                    .addComponent(lbRssProces1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                    .addGroup(jlRecursosLayout.createSequentialGroup()
                                                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                                                        .addComponent(lbNomeProces1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                            .addComponent(lbCpuProces1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                            .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(lbIdProces2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(lbMemoProces2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                            .addComponent(lbVszProces2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                            .addComponent(lbRssProces2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                            .addGroup(jlRecursosLayout.createSequentialGroup()
                                                                                .addGap(0, 1, Short.MAX_VALUE)
                                                                                .addComponent(lbNomeProces2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                    .addComponent(lbCpuProces2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addComponent(lbIdProces3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(lbCpuProces3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(lbMemoProces3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(lbVszProces3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                    .addComponent(lbRssProces3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                            .addGroup(jlRecursosLayout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(lbNomeProces3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(lbIdProces4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lbCpuProces5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lbMemoProces4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(lbVszProces4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(lbRssProces4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                    .addGroup(jlRecursosLayout.createSequentialGroup()
                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        .addComponent(lbNomeProces4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lbIdProces5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lbCpuProces4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(lbVszProces5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(lbRssProces5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGroup(jlRecursosLayout.createSequentialGroup()
                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        .addComponent(lbNomeProces5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                            .addComponent(lbMemoProces5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbIdProces6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lbCpuProces6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lbMemoProces6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lbVszProces6, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                                            .addComponent(lbRssProces6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(jlRecursosLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(lbNomeProces6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbVszProces7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbRssProces7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jlRecursosLayout.createSequentialGroup()
                                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lbIdProces7, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbMemoProces7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(lbNomeProces7, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 2, Short.MAX_VALUE))))
                            .addComponent(lbCpuProces7, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jlRecursosLayout.createSequentialGroup()
                                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbIdProces8, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbMemoProces8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 3, Short.MAX_VALUE))
                            .addComponent(lbVszProces8, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                            .addComponent(lbRssProces8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbNomeProces8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(lbCpuProces8, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jlRecursosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbIdProces9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbNomeProces9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbCpuProces9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbMemoProces9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbVszProces9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbRssProces9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        jLayeredPane1.add(jlRecursos, "card5");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setText("ID GMUD:");

        jButton5.setText("Buscar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setText("Iniciar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Finalizar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        taLog.setEditable(false);
        taLog.setColumns(20);
        taLog.setRows(5);
        jScrollPane2.setViewportView(taLog);

        jButton9.setText("Gerar Relatório");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jlGmudLayout = new javax.swing.GroupLayout(jlGmud);
        jlGmud.setLayout(jlGmudLayout);
        jlGmudLayout.setHorizontalGroup(
            jlGmudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jlGmudLayout.createSequentialGroup()
                .addGroup(jlGmudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jlGmudLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jlGmudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jlGmudLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(lbMensagem, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jlGmudLayout.createSequentialGroup()
                                .addGroup(jlGmudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jlGmudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jlGmudLayout.createSequentialGroup()
                                        .addComponent(tfIdGmud, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 178, Short.MAX_VALUE))
                                    .addGroup(jlGmudLayout.createSequentialGroup()
                                        .addComponent(jButton8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton9))))))
                    .addGroup(jlGmudLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );
        jlGmudLayout.setVerticalGroup(
            jlGmudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jlGmudLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jlGmudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jlGmudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tfIdGmud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5))
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbMensagem, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jlGmudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton8)
                    .addComponent(jButton9))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jLayeredPane1.add(jlGmud, "card6");

        jMenu1.setText("Home");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        jMenu3.setText("Recursos");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu3);

        jMenu2.setText("GMUD");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        mudarTela(jlLogin);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if ("admin".equals(lbLogin.getText())
                && "senha".equals(new String((pfSenha.getPassword())))) {
            logado = true;
            mudarTela(jlHome);
        } else {
            JOptionPane.showMessageDialog(null, "Dados inseridos não estão corretos");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        if (validacaoLogin()) {
            mudarTela(jlHome);
        }
    }//GEN-LAST:event_jMenu1MouseClicked

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        if (validacaoLogin()) {
            mudarTela(jlRecursos);
        }
    }//GEN-LAST:event_jMenu3MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        mudarTela(jlInicial);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        info = new Oshi();
        pbCpu.setValue(info.getCpuUtilizada());
        pbMemoria.setValue(info.getMemoriaUtilizada());
        lbNumeroProcess.setText(info.getNumeroProcessos());

        //<editor-fold defaultstate="collapsed" desc="Exibir ID do processo">
        lbIdProces.setText(info.getIdProcesso().get(0).toString());
        lbIdProces1.setText(info.getIdProcesso().get(1).toString());
        lbIdProces2.setText(info.getIdProcesso().get(2).toString());
        lbIdProces3.setText(info.getIdProcesso().get(3).toString());
        lbIdProces4.setText(info.getIdProcesso().get(4).toString());
        lbIdProces5.setText(info.getIdProcesso().get(5).toString());
        lbIdProces6.setText(info.getIdProcesso().get(6).toString());
        lbIdProces7.setText(info.getIdProcesso().get(7).toString());
        lbIdProces8.setText(info.getIdProcesso().get(8).toString());
        lbIdProces9.setText(info.getIdProcesso().get(9).toString());
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Exibir Cpu utilizada por processo">
        lbCpuProces.setText(info.getCpuUsadaProce().get(0).toString());
        lbCpuProces1.setText(info.getCpuUsadaProce().get(1).toString());
        lbCpuProces2.setText(info.getCpuUsadaProce().get(2).toString());
        lbCpuProces3.setText(info.getCpuUsadaProce().get(3).toString());
        lbCpuProces4.setText(info.getCpuUsadaProce().get(4).toString());
        lbCpuProces5.setText(info.getCpuUsadaProce().get(5).toString());
        lbCpuProces6.setText(info.getCpuUsadaProce().get(6).toString());
        lbCpuProces7.setText(info.getCpuUsadaProce().get(7).toString());
        lbCpuProces8.setText(info.getCpuUsadaProce().get(8).toString());
        lbCpuProces9.setText(info.getCpuUsadaProce().get(9).toString());
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Exibir Memória utilizada por processo">
        lbMemoProces.setText(info.getMemoriaUsadaProce().get(0).toString());
        lbMemoProces1.setText(info.getMemoriaUsadaProce().get(1).toString());
        lbMemoProces2.setText(info.getMemoriaUsadaProce().get(2).toString());
        lbMemoProces3.setText(info.getMemoriaUsadaProce().get(3).toString());
        lbMemoProces4.setText(info.getMemoriaUsadaProce().get(4).toString());
        lbMemoProces5.setText(info.getMemoriaUsadaProce().get(5).toString());
        lbMemoProces6.setText(info.getMemoriaUsadaProce().get(6).toString());
        lbMemoProces7.setText(info.getMemoriaUsadaProce().get(7).toString());
        lbMemoProces8.setText(info.getMemoriaUsadaProce().get(8).toString());
        lbMemoProces9.setText(info.getMemoriaUsadaProce().get(9).toString());
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Exibir VSZ">
        lbVszProces.setText(info.getVszProce().get(0));
        lbVszProces1.setText(info.getVszProce().get(1));
        lbVszProces2.setText(info.getVszProce().get(2));
        lbVszProces3.setText(info.getVszProce().get(3));
        lbVszProces4.setText(info.getVszProce().get(4));
        lbVszProces5.setText(info.getVszProce().get(5));
        lbVszProces6.setText(info.getVszProce().get(6));
        lbVszProces7.setText(info.getVszProce().get(7));
        lbVszProces8.setText(info.getVszProce().get(8));
        lbVszProces9.setText(info.getVszProce().get(9));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Exibir RSS">
        lbRssProces.setText(info.getRssProce().get(0));
        lbRssProces1.setText(info.getRssProce().get(1));
        lbRssProces2.setText(info.getRssProce().get(2));
        lbRssProces3.setText(info.getRssProce().get(3));
        lbRssProces4.setText(info.getRssProce().get(4));
        lbRssProces5.setText(info.getRssProce().get(5));
        lbRssProces6.setText(info.getRssProce().get(6));
        lbRssProces7.setText(info.getRssProce().get(7));
        lbRssProces8.setText(info.getRssProce().get(8));
        lbRssProces9.setText(info.getRssProce().get(9));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Exibir Nome processo">
        lbNomeProces.setText(info.getNomeProce().get(0));
        lbNomeProces1.setText(info.getNomeProce().get(1));
        lbNomeProces2.setText(info.getNomeProce().get(2));
        lbNomeProces3.setText(info.getNomeProce().get(3));
        lbNomeProces4.setText(info.getNomeProce().get(4));
        lbNomeProces5.setText(info.getNomeProce().get(5));
        lbNomeProces6.setText(info.getNomeProce().get(6));
        lbNomeProces7.setText(info.getNomeProce().get(7));
        lbNomeProces8.setText(info.getNomeProce().get(8));
        lbNomeProces9.setText(info.getNomeProce().get(9));
        //</editor-fold>
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
        if (validacaoLogin()) {
            mudarTela(jlGmud);
        }
    }//GEN-LAST:event_jMenu2MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        lbMensagem.setText(buscarGmud(Integer.valueOf(tfIdGmud.getText())));
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        try {

            Integer valorGmud = Integer.valueOf(tfIdGmud.getText());
            mensagemSlack();
            gerarTXT(" -- Mensagem enviada no Slack com sucesso --");

            connection = DriverManager.getConnection(url);
            String schema = connection.getSchema();
            gerarTXT("-- Conexão realizada com o banco com sucesso --");

            // Create and execute a SELECT SQL statement.
            String selectSql = "update gmud set fkStatus = 2 "
                    + "WHERE idGmud = " + valorGmud;

            Statement statement = connection.createStatement();
            statement.executeLargeUpdate(selectSql);
            gerarTXT("-- Status da GMUD foi atualizado para em execução --");

            info = new Oshi();
            gerarTXT("-- Dados da maquina captados com sucesso -- ");
            Integer CpuOshi = info.getCpuUtilizada();
            Integer MemoriaOshi = info.getMemoriaUtilizada();

            String inserindoDados = "INSERT INTO monitoramento(cpus,memoria,dataHora,fkEquipamento)"
                    +"VALUES("+ CpuOshi+","+MemoriaOshi+",getdate(),32019)";

            statement.executeLargeUpdate(inserindoDados);
            gerarTXT("-- Informações da maquina foram adicionadas ao banco --");

            // Print results from select statement
            connection.close();
            gerarTXT("-- Conexão com banco encerrada --");
            
            taLog.append("A GMUD " +valorGmud+ " iniciada no horario " +LocalDateTime.now()+"\n"
                    + "Recursos do momento do iniciada:\n"
                    + "CPU: " +CpuOshi+ "%\n"
                    + "Memória: " +MemoriaOshi+ "%\n"); 
        } 
        catch (Exception ex) {
            Logger.getLogger(TelasOshi.class.getName()).log(Level.SEVERE, null, ex);
            try {
                gerarTXT("-- Erro na inicialização da GMUD -- " + ex.toString());
            } catch (IOException ex1) {
                Logger.getLogger(TelasOshi.class.getName()).log(Level.SEVERE, null, ex1);
            } 
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:

        try {

            Integer valorGmud = Integer.valueOf(tfIdGmud.getText());
           mensagemSlackFinalizado();
            gerarTXT("-- Mensagem de finalização foi enviada pelo Slack com sucesso --");

            connection = DriverManager.getConnection(url);
            String schema = connection.getSchema();
            gerarTXT("-- Conexão com o banco realizada com sucesso --");

            // Create and execute a SELECT SQL statement.
            String selectSql = "update gmud set fkStatus = 3 "
                    + "WHERE idGmud = " + valorGmud;

            Statement statement = connection.createStatement();
            statement.executeLargeUpdate(selectSql); 
            gerarTXT("-- Informações no banco foram atualizadas com sucesso --");
          // Print results from select statement
          
            connection.close();
            gerarTXT("-- Conexão com o banco foi finalizada --");
            
            //taLog
            
            //GMUD "idGmud" iniciada pelo "motivo", no horario "time".
            //Recursos no momento do inicio:
            //cpu, memoria

        } catch (Exception ex) {
            Logger.getLogger(TelasOshi.class.getName()).log(Level.SEVERE, null, ex);
            try {
                gerarTXT("Erro:" + ex.toString());
            } catch (IOException ex1) {
                Logger.getLogger(TelasOshi.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try {
            gerarLOG(taLog.getText());
        } catch (IOException ex) {
            Logger.getLogger(TelasOshi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelasOshi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelasOshi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelasOshi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelasOshi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelasOshi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel jlGmud;
    private javax.swing.JPanel jlHome;
    private javax.swing.JPanel jlInicial;
    private javax.swing.JPanel jlLogin;
    private javax.swing.JPanel jlRecursos;
    private javax.swing.JLabel lbCpuProces;
    private javax.swing.JLabel lbCpuProces1;
    private javax.swing.JLabel lbCpuProces2;
    private javax.swing.JLabel lbCpuProces3;
    private javax.swing.JLabel lbCpuProces4;
    private javax.swing.JLabel lbCpuProces5;
    private javax.swing.JLabel lbCpuProces6;
    private javax.swing.JLabel lbCpuProces7;
    private javax.swing.JLabel lbCpuProces8;
    private javax.swing.JLabel lbCpuProces9;
    private javax.swing.JLabel lbIdProces;
    private javax.swing.JLabel lbIdProces1;
    private javax.swing.JLabel lbIdProces2;
    private javax.swing.JLabel lbIdProces3;
    private javax.swing.JLabel lbIdProces4;
    private javax.swing.JLabel lbIdProces5;
    private javax.swing.JLabel lbIdProces6;
    private javax.swing.JLabel lbIdProces7;
    private javax.swing.JLabel lbIdProces8;
    private javax.swing.JLabel lbIdProces9;
    private javax.swing.JTextField lbLogin;
    private javax.swing.JLabel lbMemoProces;
    private javax.swing.JLabel lbMemoProces1;
    private javax.swing.JLabel lbMemoProces2;
    private javax.swing.JLabel lbMemoProces3;
    private javax.swing.JLabel lbMemoProces4;
    private javax.swing.JLabel lbMemoProces5;
    private javax.swing.JLabel lbMemoProces6;
    private javax.swing.JLabel lbMemoProces7;
    private javax.swing.JLabel lbMemoProces8;
    private javax.swing.JLabel lbMemoProces9;
    private javax.swing.JLabel lbMensagem;
    private javax.swing.JLabel lbNomeProces;
    private javax.swing.JLabel lbNomeProces1;
    private javax.swing.JLabel lbNomeProces2;
    private javax.swing.JLabel lbNomeProces3;
    private javax.swing.JLabel lbNomeProces4;
    private javax.swing.JLabel lbNomeProces5;
    private javax.swing.JLabel lbNomeProces6;
    private javax.swing.JLabel lbNomeProces7;
    private javax.swing.JLabel lbNomeProces8;
    private javax.swing.JLabel lbNomeProces9;
    private javax.swing.JLabel lbNumeroProcess;
    private javax.swing.JLabel lbRssProces;
    private javax.swing.JLabel lbRssProces1;
    private javax.swing.JLabel lbRssProces2;
    private javax.swing.JLabel lbRssProces3;
    private javax.swing.JLabel lbRssProces4;
    private javax.swing.JLabel lbRssProces5;
    private javax.swing.JLabel lbRssProces6;
    private javax.swing.JLabel lbRssProces7;
    private javax.swing.JLabel lbRssProces8;
    private javax.swing.JLabel lbRssProces9;
    private javax.swing.JLabel lbVszProces;
    private javax.swing.JLabel lbVszProces1;
    private javax.swing.JLabel lbVszProces2;
    private javax.swing.JLabel lbVszProces3;
    private javax.swing.JLabel lbVszProces4;
    private javax.swing.JLabel lbVszProces5;
    private javax.swing.JLabel lbVszProces6;
    private javax.swing.JLabel lbVszProces7;
    private javax.swing.JLabel lbVszProces8;
    private javax.swing.JLabel lbVszProces9;
    private javax.swing.JProgressBar pbCpu;
    private javax.swing.JProgressBar pbMemoria;
    private javax.swing.JPasswordField pfSenha;
    private javax.swing.JTextArea taLog;
    private javax.swing.JTextField tfIdGmud;
    // End of variables declaration//GEN-END:variables
}
