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
        cartas.add(null);
        cartas.add(null);
        cartas.add(null);
    }
    
    Jugador(String nombre, int id){
        this.nombre = nombre;
        this.id = id;
        cartas.add(null);
        cartas.add(null);
        cartas.add(null);
    }
    
    public void guardarStringCartas(String cadena){
        for(int x=0;x<3;x++){
            String cta = cadena.substring(x*3, (x*3+3));
            Carta cart1 = new Carta(cta.charAt(0),Integer.parseInt(cta.substring(1,3)));
            cartas.set(x, cart1);
        }
    }
    
    public String cartasToString(List<Carta> cartas){
        String msj="";
        for(int x=0;x<3;x++){
            msj+=cartas.get(x).getPalo();
            int numero=cartas.get(x).getNumero();
            if(numero<=9){msj+="0"+numero;   
            } else{msj+=numero;}
        }
        return msj;
    }
    
    //<editor-fold defaultstate="collapsed" desc="GetSet">

    public void setCartas(List<Carta> Cartas) {
        //Cartas.get(0)
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
