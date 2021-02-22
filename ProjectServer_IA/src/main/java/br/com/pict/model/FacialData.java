/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pict.model;

import java.util.Date;

/**
 *
 * @author Iarley
 */
public class FacialData {
    
    private Integer id; 
    private String tipoObjeto;
    private byte[] arquivo; 
    private Date dataRegistro;

//  =========================================== JUNÇÕES N-1

//  =========================================== JUNÇÕES 1-N

//  =========================================== JUNÇÕES 1-1

    private Pessoa pessoa;
    
//  =========================================== CONSTRUTORES

//  =========================================== TOSTRING, COMPARE, GETSETS E OUTROS

    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    public String getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(String tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
    
    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}
