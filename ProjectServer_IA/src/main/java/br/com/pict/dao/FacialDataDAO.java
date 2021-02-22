
package br.com.pict.dao;

import br.com.pict.database.DatabaseMySQL;
import br.com.pict.model.FacialData;
import br.com.pict.model.Pessoa;

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

        String sql = "SELECT * FROM `sslfb_data`.`facial_data` as fd WHERE fd.id = "+id+" ORDER BY fd.id DESC";

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

        String sql = "SELECT * FROM `sslfb_data`.`facial_data` as fd ORDER BY fd.id DESC";

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

    public FacialData findFromRecognize(Integer id){
        Connection connection = DatabaseMySQL.connectionData();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String sql = "SELECT * FROM sslfb.pessoa AS p " +
                "LEFT JOIN sslfb_data.facial_data as fd ON(fd.id_pessoa = p.id) " +
                "WHERE p.id = "+id+" LIMIT 1";

        FacialData facialData = new FacialData();
        try {
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            if(resultSet.getInt("p.id") != 0){
                facialData.setId(resultSet.getInt("fd.id"));
                facialData.setDataRegistro(resultSet.getDate("fd.dataRegistro"));
                facialData.setArquivo(resultSet.getBytes("fd.arquivo"));
                facialData.setTipoObjeto(resultSet.getString("fd.tipoObjeto"));

                Pessoa pessoa = new Pessoa();
                pessoa.setId(resultSet.getInt("p.id"));
                pessoa.setCpf(resultSet.getString("p.cpf"));
                pessoa.setNome(resultSet.getString("p.nome"));
                pessoa.setSobrenome(resultSet.getString("p.sobrenome"));

                facialData.setPessoa(pessoa);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FacialDataDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseMySQL.closeConnection(connection, statement);
        }

        return facialData;
    }

    public List<FacialData> findAllLastImages(){
        Connection connection = DatabaseMySQL.connectionData();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<FacialData> facialDataList = new ArrayList<>();

        String sql =
            "SELECT * FROM sslfb_data.facial_data AS fd " +
                "LEFT JOIN sslfb.pessoa AS p ON(p.id = fd.id_pessoa) "+
                "WHERE fd.id_pessoa = (SELECT id FROM sslfb.pessoa ORDER BY id DESC LIMIT 1)";

        try {
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                FacialData facialData = new FacialData();

                facialData.setId(resultSet.getInt("fd.id"));
                facialData.setDataRegistro(resultSet.getDate("fd.dataRegistro"));
                facialData.setArquivo(resultSet.getBytes("fd.arquivo"));
                facialData.setTipoObjeto(resultSet.getString("fd.tipoObjeto"));

                Pessoa pessoa = new Pessoa();
                pessoa.setAlunoMatricula(resultSet.getString("p.alunoMatricula"));
                pessoa.setAlunoCurso(resultSet.getString("p.alunoCurso"));
                pessoa.setAlunoAnoMatricula(resultSet.getInt("p.alunoAnoMatricula"));
                pessoa.setAlunoAnoSaida(resultSet.getInt("p.alunoAnoSaida"));

                pessoa.setFuncionarioCodigo(resultSet.getString("p.funcionarioCodigo"));
                pessoa.setFuncionarioDataAdmissao(resultSet.getDate("p.funcionarioDataAdmissao"));
                pessoa.setFuncionarioDataDemissao(resultSet.getDate("p.funcionarioDataDemissao"));

                pessoa.setId(resultSet.getInt("p.id"));
                pessoa.setNome(resultSet.getString("p.nome"));
                pessoa.setSobrenome(resultSet.getString("p.sobrenome"));
                pessoa.setEmail(resultSet.getString("p.email"));
                pessoa.setNascimento(resultSet.getDate("p.nascimento"));
                pessoa.setCpf(resultSet.getString("p.cpf"));
                pessoa.setRg(resultSet.getString("p.rg"));

                facialData.setPessoa(pessoa);

                facialDataList.add(facialData);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FacialDataDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseMySQL.closeConnection(connection, statement);
        }

        return facialDataList;
    }
}
