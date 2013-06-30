/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author Joshua
 */
public class Jugador {
    private String nombre;
    private int id;

    Jugador(){
        
    }
    
    Jugador(String nombre, int id){
        this.nombre = nombre;
        this.id = id;
    }
    
    //<editor-fold defaultstate="collapsed" desc="GetSet">
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    //</editor-fold>
    
    
}
