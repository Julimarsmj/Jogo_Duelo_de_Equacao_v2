/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.duelo_equacoes.dal;

import java.sql.*;

/**
 *
 * @author Julimar
 */
public class ModeloConexao {

    public static Connection conectar() {

        Connection conexao = null;
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/dbjogador";
        String user = "root";
        String password = "21041997";

        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            return null;
        }

    }

}
