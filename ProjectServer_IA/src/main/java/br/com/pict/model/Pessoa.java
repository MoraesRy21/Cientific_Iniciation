package br.com.pict.model;

import java.util.Date;
import javafx.scene.control.CheckBox;

/**
 *
 * @author Iarley
 */
public class Pessoa {
    
    private CheckBox checkBox = new CheckBox();
    
    private Integer id;
    private String nome;
    private String sobrenome;
    private Date nascimento;
    private String email;
    private String telefone;
    private String cpf;
    private String rg;
    
    //Aluno
    private String alunoMatricula;
    private String alunoCurso;
    private Integer alunoAnoMatricula;
    private Integer alunoAnoSaida;
    
    //Funcionário
    private String funcionarioCodigo;
    private Date funcionarioDataAdmissao;
    private Date funcionarioDataDemissao;

//  =========================================== JUNÇÕES N-1
	
//  =========================================== JUNÇÕES 1-N
	
//  =========================================== JUNÇÕES 1-1
    
//  =========================================== CONSTRUTORES

    public Pessoa(){
    }

    public Pessoa(Integer id){
        this.id = id;
    }

    /**
     * Para pessoas que não são nem alunos nem funcionários
     * @param id
     * @param nome
     * @param sobrenome
     * @param nascimento
     * @param email
     * @param telefone
     * @param cpf
     * @param rg
     */
    public Pessoa(Integer id, String nome, String sobrenome, Date nascimento, String email, String telefone, 
            String cpf, String rg) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.nascimento = nascimento;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.rg = rg;
    }
    
    /**
     * Para pessoas que são somente alunos
     * @param id
     * @param nome
     * @param sobrenome
     * @param nascimento
     * @param email
     * @param telefone
     * @param cpf
     * @param rg
     * @param alunoMatricula
     * @param alunoCurso
     * @param alunoAnoMatricula
     * @param alunoAnoSaida 
     */
    public Pessoa(Integer id, String nome, String sobrenome, Date nascimento, String email, String telefone, 
            String cpf, String rg, String alunoMatricula, String alunoCurso, Integer alunoAnoMatricula, Integer alunoAnoSaida) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.nascimento = nascimento;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.rg = rg;
        this.alunoAnoMatricula = alunoAnoMatricula;
        this.alunoCurso = alunoCurso;
        this.alunoAnoMatricula = alunoAnoMatricula;
        this.alunoAnoSaida = alunoAnoSaida;
    }
    
    /**
     * Para pessoas que são somente Funcionários 
     * @param id
     * @param nome
     * @param sobrenome
     * @param nascimento
     * @param email
     * @param telefone
     * @param cpf
     * @param rg
     * @param funcionarioCodigo
     * @param funcionarioDataAdmissao
     * @param funcionarioDataDemissao 
     */
    public Pessoa(Integer id, String nome, String sobrenome, Date nascimento, String email, String telefone, 
            String cpf, String rg, String funcionarioCodigo, Date funcionarioDataAdmissao, Date funcionarioDataDemissao) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.nascimento = nascimento;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.rg = rg;
        this.funcionarioCodigo = funcionarioCodigo;
        this.funcionarioDataAdmissao = funcionarioDataAdmissao;
        this.funcionarioDataDemissao = funcionarioDataDemissao;
    }

    /**
     * Para pessoas que são Alunos e Funcionários
     * @param id
     * @param nome
     * @param sobrenome
     * @param nascimento
     * @param email
     * @param telefone
     * @param cpf
     * @param rg
     * @param alunoMatricula
     * @param alunoCurso
     * @param alunoAnoMatricula
     * @param alunoAnoSaida
     * @param funcionarioCodigo
     * @param funcionarioDataAdmissao
     * @param funcionarioDataDemissao 
     */
    public Pessoa(Integer id, String nome, String sobrenome, Date nascimento, String email, String telefone, String cpf, String rg, 
            String alunoMatricula, String alunoCurso, Integer alunoAnoMatricula, Integer alunoAnoSaida, 
            String funcionarioCodigo, Date funcionarioDataAdmissao, Date funcionarioDataDemissao) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.nascimento = nascimento;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.rg = rg;
        this.alunoMatricula = alunoMatricula;
        this.alunoCurso = alunoCurso;
        this.alunoAnoMatricula = alunoAnoMatricula;
        this.alunoAnoSaida = alunoAnoSaida;
        this.funcionarioCodigo = funcionarioCodigo;
        this.funcionarioDataAdmissao = funcionarioDataAdmissao;
        this.funcionarioDataDemissao = funcionarioDataDemissao;
    }
    
    //Para testes no PerfisManagerCOntroller
    public Pessoa(CheckBox checkBox,Integer id, String nome) {
        this.checkBox = checkBox;
        this.id = id;
        this.nome = nome;
    }
    
//  =========================================== TOSTRING, COMPARE, GETSETS E OUTROS

    public boolean isAluno(){
        return alunoMatricula != null || alunoCurso != null || alunoAnoMatricula != null || alunoAnoSaida != null;
    }

    public boolean isFuncionario(){
        return funcionarioCodigo != null || funcionarioDataAdmissao != null || funcionarioDataDemissao != null;
    }
    
    @Override
    public String toString() {
        return nome+" "+sobrenome;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }
    
    public String getNomeCompleto(){
        return nome+" "+sobrenome;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getAlunoMatricula() {
        return alunoMatricula;
    }

    public void setAlunoMatricula(String alunoMatricula) {
        this.alunoMatricula = alunoMatricula;
    }

    public String getAlunoCurso() {
        return alunoCurso;
    }

    public void setAlunoCurso(String alunoCurso) {
        this.alunoCurso = alunoCurso;
    }

    public int getAlunoAnoMatricula() {
        return alunoAnoMatricula;
    }

    public void setAlunoAnoMatricula(Integer alunoAnoMatricula) {
        this.alunoAnoMatricula = alunoAnoMatricula;
    }

    public int getAlunoAnoSaida() {
        return alunoAnoSaida;
    }

    public void setAlunoAnoSaida(Integer alunoAnoSaida) {
        this.alunoAnoSaida = alunoAnoSaida;
    }

    public String getFuncionarioCodigo() {
        return funcionarioCodigo;
    }

    public void setFuncionarioCodigo(String funcionarioCodigo) {
        this.funcionarioCodigo = funcionarioCodigo;
    }

    public Date getFuncionarioDataAdmissao() {
        return funcionarioDataAdmissao;
    }

    public void setFuncionarioDataAdmissao(Date funcionarioDataAdmissao) {
        this.funcionarioDataAdmissao = funcionarioDataAdmissao;
    }

    public Date getFuncionarioDataDemissao() {
        return funcionarioDataDemissao;
    }

    public void setFuncionarioDataDemissao(Date funcionarioDataDemissao) {
        this.funcionarioDataDemissao = funcionarioDataDemissao;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
    
}
