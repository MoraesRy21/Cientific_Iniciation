
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
public class LoginDAO {
    
    protected void save(Login login) throws SQLException{
        Connection connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;
        
        String sql = "INSERT INTO login values (?,?)";
        
        statement = connection.prepareStatement(sql);
        statement.setString(1, login.getUsuario());
        statement.setString(2, login.getSenha());
        statement.executeUpdate();
        
        DatabaseMySQL.closeConnection(connection, statement);
    }
    
    protected void updateSenha(String usuario, String senha) throws SQLException{
        Connection connection = connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;
        
        String sql = "UPDATE login SET senha = ? WHERE usuario = ?";
        
        statement = connection.prepareStatement(sql);
        statement.setString(1, senha);
        statement.setString(2, usuario);
        statement.executeUpdate();
        
        DatabaseMySQL.closeConnection(connection, statement);
    }
    
    public static List<Login> authenticationLogin(Login loginController){
        
        Connection connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        @SuppressWarnings("Convert2Diamond")
        List<Login> loginList = new ArrayList<Login>();
        
        String sql = "SELECT usuario, senha FROM login where usuario = ? and senha = ?";
        try{
            statement = connection.prepareStatement(sql);
            statement.setString(1, loginController.getUsuario());
            statement.setString(2, loginController.getSenha());
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                Login loginTemp = new Login();
                loginTemp.setUsuario(resultSet.getString("usuario"));
                loginTemp.setSenha(resultSet.getString("senha"));
                loginList.add(loginTemp);
            }
        }catch(SQLException ex){
            System.out.println("Query deu erro: "+ex);
        }finally{
            DatabaseMySQL.closeConnection(connection, statement, resultSet);
        }
        return loginList;
    }
}
