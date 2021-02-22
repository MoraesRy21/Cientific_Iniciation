/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pict.dao;

import br.com.pict.database.DatabaseMySQL;
import br.com.pict.model.FacialData;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Iarley
 */
public class FacialDataDAO implements DAO<FacialData> {

    PessoaDAO pessoaDAO = new PessoaDAO();
    
    @Override
    public FacialData findById(Integer id) {
        Connection connection = DatabaseMySQL.connectionData();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String sql = "SELECT * FROM `sslfb`.`facial_data` as fd WHERE fd.id = "+id+" ORDER BY fd.id DESC";

        FacialData facialData = new FacialData();
        try {
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            if(resultSet.getInt("fd.id") != 0){
                facialData.setId(resultSet.getInt("fd.id"));
                facialData.setDataRegistro(resultSet.getDate("fd.dataRegistro"));
                facialData.setArquivo(resultSet.getBytes("fd.arquivo"));
                facialData.setTipoObjeto(resultSet.getString("fd.tipoObjeto"));
                
                facialData.setPessoa(pessoaDAO.findById(resultSet.getInt("fd.id_pessoa")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(FacialDataDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseMySQL.closeConnection(connection, statement);
        }
        
        return facialData;
    }

    @Override
    public List<FacialData> findAll() {
        Connection connection = DatabaseMySQL.connectionData();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<FacialData> facialDataList = new ArrayList<>();

        String sql = "SELECT * FROM `sslfb`.`facial_data` as fd ORDER BY fd.id DESC";

        try {
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                FacialData facialData = new FacialData();
                
                facialData.setId(resultSet.getInt("fd.id"));
                facialData.setDataRegistro(resultSet.getDate("fd.dataRegistro"));
                facialData.setArquivo(resultSet.getBytes("fd.arquivo"));
                facialData.setTipoObjeto(resultSet.getString("fd.tipoObjeto"));
                
                facialData.setPessoa(pessoaDAO.findById(resultSet.getInt("fd.id_pessoa")));
                
                facialDataList.add(facialData);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FacialDataDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseMySQL.closeConnection(connection, statement);
        }
        
        return facialDataList;
    }

    @Override
    public void create(FacialData facialData) {
        Connection connection = DatabaseMySQL.connectionData();
        PreparedStatement statement = null;
        String pessoaSQL = "(SELECT MAX(id) FROM `sslfb`.`pessoa` as p WHERE p.`CPF` = " + facialData.getPessoa().getCpf() + ")";

        String sql = "INSERT INTO `facial_data`(id_pessoa, id, arquivo, tipoObjeto) VALUE(" + pessoaSQL + ", DEFAULT, ?, ?)";

        try {
            statement = connection.prepareStatement(sql);

            statement.setBlob(1, new ByteArrayInputStream(facialData.getArquivo()));
            statement.setString(2, facialData.getTipoObjeto());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FacialDataDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseMySQL.closeConnection(connection, statement);
        }
    }

    @Override
    public void delete(FacialData facialData) {
        Connection connection = DatabaseMySQL.connectionData();
        PreparedStatement statement = null;

        String sql = "DELETE FROM `facial_data` WHERE id_pessoa = ?";

        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, (int) facialData.getPessoa().getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FacialDataDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseMySQL.closeConnection(connection, statement);
        }
    }

    @Override
    public void update(FacialData newE, FacialData oldE) {
    }

}
