package br.com.pict.dao;

import br.com.pict.database.DatabaseMySQL;
import br.com.pict.model.Compose;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Iarley
 */
public class ComposeDAO implements DAO<Compose>{

    @Override
    public Compose findById(Integer id) {
        Connection connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        String sql = 
                "SELECT * FROM `sslfb`.`pessoa` as p "
                + "LEFT JOIN `sslfb_data`.`facial_data` AS f ON(f.id_pessoa = p.id)"
                + "WHERE p.id = " + id + " "
                + "ORDER BY p.id LIMIT 1";
        Compose compose = new Compose();
        
        try{
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                if(resultSet != null && resultSet.getInt("p.id") != 0){
                    compose.setId(resultSet.getInt("p.id"));
                    compose.setNome(resultSet.getString("p.nome"));
                    compose.setSobrenome(resultSet.getString("p.sobrenome"));
                    compose.setEmail(resultSet.getString("p.email"));
                    compose.setNascimento(resultSet.getDate("p.nascimento"));
                    compose.setCpf(resultSet.getString("p.cpf"));
                    compose.setRg(resultSet.getString("p.rg"));

                    compose.setArquivo(resultSet.getBytes("f.arquivo"));
                }else{
                    System.out.println("resultSet null");
                    compose = null;
                }
            }
                
        } catch (SQLException ex) {
            Logger.getLogger(ComposeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            DatabaseMySQL.closeConnection(connection, statement, resultSet);
        }
        
        return compose;
    }

    @Override
    public List<Compose> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Compose e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Compose e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Compose newE, Compose oldE) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
