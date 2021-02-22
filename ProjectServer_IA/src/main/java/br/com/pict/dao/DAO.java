/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pict.dao;


import java.util.List;

/**
 *
 * @author Iarley
 */
public interface DAO<E> {
    
    public E findById(Integer id);
    
    public List<E> findAll();
    
    public void create(E e);
    
    public void delete(E e);
    
    public void update(E newE, E oldE);
    
}
