/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pict.database;

import br.com.pict.util.ConfigFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Iarley
 */
public class DatabaseMySQL {
    
    

    /**
     * Connection with main database
     * @return 
     */
    public static Connection connection() {
        try{
            Connection connection;
            
            Class.forName(ConfigFactory.extract_sslfb.get("driver"));
            connection = DriverManager.getConnection(ConfigFactory.extract_sslfb.get("url"), 
                    ConfigFactory.extract_sslfb.get("username"), ConfigFactory.extract_sslfb.get("password"));
            return connection;
            
        }catch(SQLException | ClassNotFoundException ex){
            System.out.println("Não foi possível conecter ao banco MySQL: "+ ex);
            //Criar classe para tratar deste erro
            return null;
        }
    }
    
    /**
     * Connection with other database with data files
     * @return 
     */
    public static Connection connectionData() {
        try{
            Connection connection;
            
            Class.forName(ConfigFactory.extract_sslfb_data.get("driver"));
            connection = DriverManager.getConnection(ConfigFactory.extract_sslfb_data.get("url"), 
                    ConfigFactory.extract_sslfb_data.get("username"), ConfigFactory.extract_sslfb_data.get("password"));
            return connection;
            
        }catch(SQLException | ClassNotFoundException ex){
            System.out.println("Não foi possível conecter ao banco MySQL: "+ ex);
            //Criar classe para tratar deste erro
            return null;
        }
    }
    
    public static void closeConnection(Connection conn) {
        try{
            if(conn != null){
                conn.close();
            }
        }catch(SQLException ex){
            System.out.println("Não foi possível desconecter ao banco MySQL: "+ ex);
            //Criar uma classe para tratar deste erro
        }
    }
    
    public static void closeConnection(Connection conn, PreparedStatement stmt) {
        closeConnection(conn);
        try{
            if(stmt != null){
                stmt.close();
            }else{
                System.out.println("statement é nulo");
            }
        }catch(SQLException ex){
            System.out.println("Statement não pode ser fechado! Não foi possível desconecter ao banco MySQL: "+ ex);
            //Criar uma classe para tratar deste erro
        }
    }
    
    public static void closeConnection(Connection conn, PreparedStatement stm, ResultSet rs) {
        closeConnection(conn, stm);
        try{
            if(rs != null){
                rs.close();
            }
        }catch(SQLException ex){
            System.out.println("ResultSet não pode ser fechado! Não foi possível desconecter ao banco MySQL: "+ ex);
            //Criar uma classe para tratar deste erro
        }
    }
    
}
