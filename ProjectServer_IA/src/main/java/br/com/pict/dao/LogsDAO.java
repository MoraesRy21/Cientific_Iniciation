
package br.com.pict.dao;

import br.com.pict.database.DatabaseMySQL;
import br.com.pict.model.Logs;
import br.com.pict.model.Pessoa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Iarley
 */
public class LogsDAO implements DAO<Logs>{

    @Override
    public Logs findById(Integer id) {
        return null;
    }

    @Override
    public List<Logs> findAll() {
        Connection connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Logs> logsList = new ArrayList<>();
        
        String sql = 
                "SELECT * FROM `log` as l LEFT JOIN `pessoa` as p ON (l.id_pessoa = p.id) ORDER BY p.id DESC";
        
        try{
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                Logs logs = new Logs();
                Pessoa pessoa = new Pessoa();
                
                pessoa.setId(resultSet.getInt("p.id"));
                pessoa.setNome(resultSet.getString("p.nome"));
                pessoa.setSobrenome(resultSet.getString("p.sobrenome"));
                pessoa.setEmail(resultSet.getString("p.email"));
                pessoa.setNascimento(resultSet.getDate("p.nascimento"));
                pessoa.setCpf(resultSet.getString("p.cpf"));
                pessoa.setRg(resultSet.getString("p.rg"));
                
                pessoa.setAlunoMatricula(resultSet.getString("p.alunoMatricula"));
                pessoa.setAlunoCurso(resultSet.getString("p.alunoCurso"));
                pessoa.setAlunoAnoMatricula(resultSet.getInt("p.alunoAnoMatricula"));
                pessoa.setAlunoAnoSaida(resultSet.getInt("p.alunoAnoSaida"));
                
                pessoa.setFuncionarioCodigo(resultSet.getString("p.funcionarioCodigo"));
                pessoa.setFuncionarioDataAdmissao(resultSet.getDate("p.funcionarioDataAdmissao"));
                pessoa.setFuncionarioDataDemissao(resultSet.getDate("p.funcionarioDataDemissao"));
                
                logs.setId(resultSet.getInt("l.id"));
                logs.setDataEntrada(resultSet.getDate("l.dataEntrada"));
                logs.setDataSaida(resultSet.getDate("l.dataSaida"));
                
                logs.setPessoa(pessoa);
                
                logsList.add(logs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LogsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            DatabaseMySQL.closeConnection(connection, statement, resultSet);
        }
        
        return logsList;
    }

    @Override
    public void create(Logs e) {
        Connection connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;

        String sql =
                "INSERT INTO sslfb.logs (id_pessoa, id, dataEntrada, dataSaida) VALUES (?, DEFAULT, ?, ?)";
        try{
            statement = connection.prepareStatement(sql);

            statement.setInt(1, e.getPessoa().getId());
            statement.setDate(2, new java.sql.Date(e.getDataEntrada().getTime()));
            statement.setDate(3, new java.sql.Date(e.getDataSaida().getTime()));

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            DatabaseMySQL.closeConnection(connection, statement);
        }
    }

    @Override
    public void delete(Logs e) {
    }

    @Override
    public void update(Logs newE, Logs oldE) {
    }

    public List<Logs> filter(String nome, String cpf, String rg) {
        Connection connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Logs> logsList = new ArrayList<>();
        
        String filter1 = " ";
        String filter2 = " ";
        String filter3 = " ";
        
        if(nome != null || !nome.equals(""))
            filter1 = "and %"+nome+"%"+" ilike p.nome";
        if(cpf != null || !cpf.equals(""))
            filter2 = "and %"+cpf+"%"+" ilike p.CPF";
        if(rg != null || !rg.equals(""))
            filter3 = "and %"+rg+"%"+" ilike p.RG";
        
        String sql = 
                "SELECT l.id, l.dataEntrada, l.dataSaida, p.id, p.nome, p.sobrenome, p.CPF, a.matricula, fu.codigo " + 
                    "FROM `log` as l " +
                    "LEFT JOIN `pessoa` as p ON (l.id_pessoa = p.id) " +
                    "WHERE 1 " + filter1+ filter2+ filter3+
                    "ORDER BY p.id DESC";
        
        try{
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                Logs logs = new Logs();
                Pessoa pessoa = new Pessoa();
                
                pessoa.setId(resultSet.getInt("p.id"));
                pessoa.setNome(resultSet.getString("p.nome"));
                pessoa.setSobrenome(resultSet.getString("p.sobrenome"));
                pessoa.setCpf(resultSet.getString("p.cpf"));
                
                logs.setId(resultSet.getInt("l.id"));
                logs.setDataEntrada(resultSet.getDate("l.dataEntrada"));
                logs.setDataSaida(resultSet.getDate("l.dataSaida"));
                
                logs.setPessoa(pessoa);
                
                logsList.add(logs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LogsDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            DatabaseMySQL.closeConnection(connection, statement, resultSet);
        }
        
        return logsList;
    }
    
}
