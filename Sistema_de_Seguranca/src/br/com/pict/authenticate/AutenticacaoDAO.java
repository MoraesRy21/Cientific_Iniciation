/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pict.authenticate;

import br.com.pict.database.DatabaseMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Iarley
 */
public class AutenticacaoDAO {
    public static List<String> versao(){
        
        Connection connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        @SuppressWarnings("Convert2Diamond")
        List<String> versaoList = new ArrayList<>();
        
        String sql = "SELECT versao FROM autenticacao where id = 1";
        try{
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                versaoList.add(resultSet.getString("versao"));
            }
        }catch(SQLException ex){
            System.out.println("Query deu erro: "+ex);
        }finally{
            DatabaseMySQL.closeConnection(connection, statement, resultSet);
        }
        return versaoList;
    }
}
