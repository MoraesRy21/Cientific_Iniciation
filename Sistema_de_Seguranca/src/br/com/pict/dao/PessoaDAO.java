package br.com.pict.dao;

import br.com.pict.database.DatabaseMySQL;
import br.com.pict.model.Pessoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Iarley
 */
public class PessoaDAO implements DAO<Pessoa>{

    @Override
    public Pessoa findById(Integer id) {
        Connection connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        String sql = 
                "SELECT * FROM `pessoa` as p WHERE p.id = "+id +" "+
                    "ORDER BY p.id DESC";
        Pessoa pessoa = new Pessoa();
        
        try{
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            if(resultSet.getInt("p.id") != 0){
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
            }else
                pessoa = null;
                
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            DatabaseMySQL.closeConnection(connection, statement, resultSet);
        }
        
        return pessoa;
    }

    @Override
    public List<Pessoa> findAll() {
        Connection connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Pessoa> pessoaList = new ArrayList<Pessoa>();
        
        String sql = 
                "SELECT * FROM `pessoa` as p " +
                    "ORDER BY p.id DESC";
        
        try{
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
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
                
                pessoaList.add(pessoa);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            DatabaseMySQL.closeConnection(connection, statement, resultSet);
        }
        
        return pessoaList;
    }

    @Override
    public void create(Pessoa pessoa) {
        Connection connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;
        
        String sql = 
                "INSERT INTO `sslfb`.`pessoa` VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            statement = connection.prepareStatement(sql);
            statement.setString(1, pessoa.getNome());
            statement.setString(2, pessoa.getSobrenome());
            statement.setDate(3, new java.sql.Date(pessoa.getNascimento().getTime()));

            if(pessoa.getEmail() != null) 
                statement.setString(4, pessoa.getEmail());
            else 
                statement.setString(4, null);
            
            if(pessoa.getTelefone() != null) 
                statement.setString(5, pessoa.getTelefone());
            else 
                statement.setString(5, null);
            
            statement.setString(6, pessoa.getCpf());
            
            if(pessoa.getRg() != null) 
                statement.setString(7, pessoa.getRg());
            else 
                statement.setString(7, null);
            
            if(pessoa.isAluno()){
                statement.setString(8, pessoa.getAlunoMatricula());
                statement.setString(9, pessoa.getAlunoCurso());
                statement.setInt(10, pessoa.getAlunoAnoMatricula());
                statement.setInt(11, pessoa.getAlunoAnoSaida());
            }else{
                statement.setString(8, null);
                statement.setString(9, null);
                statement.setNull(10, Types.INTEGER);
                statement.setNull(11, Types.INTEGER);
            }
            
            if(pessoa.isFuncionario()){
                statement.setString(12, pessoa.getFuncionarioCodigo());
                statement.setDate(13, new java.sql.Date(pessoa.getFuncionarioDataAdmissao().getTime()));
                statement.setDate(14, new java.sql.Date(pessoa.getFuncionarioDataDemissao().getTime()));
            }else{
                statement.setString(12, null);
                statement.setDate(13, null);
                statement.setDate(14, null);
            }

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            DatabaseMySQL.closeConnection(connection, statement);
        }
    }

    @Override
    public void delete(Pessoa pessoa) {
        Connection connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;
        
        String sql = "DELETE FROM `pessoa` WHERE id = ?";
        
        try{
            statement = connection.prepareStatement(sql);
            statement.setInt(1, pessoa.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            DatabaseMySQL.closeConnection(connection, statement);
        }
    }

    @Override
    public void update(Pessoa newPessoa, Pessoa oldPessoa) {
        
    }
    
    public boolean hasLog(Pessoa pessoa){
        boolean status = false;
        Connection connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        String sql = 
                "SELECT * FROM `pessoa` as p INNER JOIN `log` as l ON(p.id = l.id_pessoa)";
        
        try{
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                if(resultSet.getInt("l.id_pessoa") == pessoa.getId()){
                    status = true;
                    break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            DatabaseMySQL.closeConnection(connection, statement, resultSet);
        }
        
        return status;
    }
    
    public List<Pessoa> filter(String nome, String cpf, String rg, Date dataNacimento){
        Connection connection = DatabaseMySQL.connection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Pessoa> pessoaList = new ArrayList<Pessoa>();
        
        String filter1 = " ";
        String filter2 = " ";
        String filter3 = " ";
        String filter4 = " ";
        
        if(nome != null || !nome.equals(""))
            filter1 = "and %"+nome+"%"+" ilike p.nome";
        if(cpf != null || !cpf.equals(""))
            filter2 = "and %"+cpf+"%"+" ilike p.CPF";
        if(rg != null || !rg.equals(""))
            filter3 = "and %"+rg+"%"+" ilike p.RG";
        if(dataNacimento != null)
            filter4 = "and p.nascimento = "+dataNacimento;
        
        String sql = 
                "SELECT * "+
                    "FROM `pessoa` as p " +
                    "WHERE 1 " + filter1+ filter2+ filter3+ filter4 +
                    "ORDER BY p.id DESC";
        
        try{
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                Pessoa pessoa = new Pessoa();
                
                pessoa.setId(resultSet.getInt("p.id"));
                pessoa.setNome(resultSet.getString("p.nome"));
                pessoa.setSobrenome(resultSet.getString("p.sobrenome"));
                pessoa.setCpf(resultSet.getString("p.CPF"));
                pessoa.setRg(resultSet.getString("p.RG"));
                pessoa.setNascimento(resultSet.getDate("p.nascimento"));
                
                pessoaList.add(pessoa);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            DatabaseMySQL.closeConnection(connection, statement, resultSet);
        }
        
        return pessoaList;
    }
    
}
