package br.com.pict.authenticate;

/**
 *
 * @author Iarley
 */
public class Login {
    
    private int id_login;
    private String usuario;
    private String senha;

    public long getId_login() {
        return id_login;
    }

    public void setId_login(int id_login) {
        this.id_login = id_login;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}
