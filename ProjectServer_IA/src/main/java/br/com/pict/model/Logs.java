package br.com.pict.model;

import java.util.Date;

/**
 *
 * @author Iarley
 */
public class Logs {
    
//  ============== Campos =================
    private Integer id;
    private Date dataEntrada;
    private Date dataSaida;
    
// ================ Relações 1 para N =======================
    private Pessoa pessoa;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    
}
