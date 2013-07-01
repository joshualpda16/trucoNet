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
    private int puntos;
    private int cartasTiradas;
    private String nombre;
    private int id;
    private List<Carta> cartas = new ArrayList();
    private int instanciasGanadas=0;
    private boolean carta1tirada,carta2tirada,carta3tirada;

    Jugador(){
        cartas.add(null);
        cartas.add(null);
        cartas.add(null);
        cartasTiradas = 0;
    }
    
    Jugador(String nombre, int id){
        this.nombre = nombre;
        this.id = id;
        cartas.add(null);
        cartas.add(null);
        cartas.add(null);
        cartasTiradas=0;
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
    
    public int getInstanciasGanadas() {
        return instanciasGanadas;
    }

    public void setInstanciasGanadas(int instanciasGanadas) {
        this.instanciasGanadas = instanciasGanadas;
    }

    public boolean isCarta1tirada() {
        return carta1tirada;
    }

    public void setCarta1tirada(boolean carta1tirada) {
        this.carta1tirada = carta1tirada;
    }

    public boolean isCarta2tirada() {
        return carta2tirada;
    }

    public void setCarta2tirada(boolean carta2tirada) {
        this.carta2tirada = carta2tirada;
    }

    public boolean isCarta3tirada() {
        return carta3tirada;
    }

    public void setCarta3tirada(boolean carta3tirada) {
        this.carta3tirada = carta3tirada;
    }
    
    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getCartasTiradas() {
        return cartasTiradas;
    }

    public void setCartasTiradas(int cartasTiradas) {
        this.cartasTiradas = cartasTiradas;
    }
    
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
