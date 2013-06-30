/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joshua
 */
public class Jugador {
    private String nombre;
    private int id;
    private List<Carta> cartas = new ArrayList();

    Jugador(){
        
    }
    
    Jugador(String nombre, int id){
        this.nombre = nombre;
        this.id = id;
    }
    
    //<editor-fold defaultstate="collapsed" desc="GetSet">

    public void setCartas(List<Carta> Cartas) {
        this.cartas = Cartas;
    }

    public List<Carta> getCartas() {
        return cartas;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    //</editor-fold>
    
    
}
