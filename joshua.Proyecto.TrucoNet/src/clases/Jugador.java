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
    
    public int contarTantos(){
        int tantos=0;
        //Si el Palo de la primera es igual al de la segunda
        int num1=cartas.get(0).getNumero();
        int num2=cartas.get(1).getNumero();
        int num3=cartas.get(2).getNumero();
        
        char palo1=cartas.get(0).getPalo();
        char palo2=cartas.get(1).getPalo();
        char palo3=cartas.get(2).getPalo();
        
        int max=0,mid=0,min = 0;
        
        //<editor-fold defaultstate="collapsed" desc="Busco max,mid,min">
        if((num1>num2)&&(num1>num3)){
            max=num1;
        } else if((num2>num1)&&(num2>num3)){
            max=num2;
        } else if((num3>num1)&&(num3>num2)){
            max=num3;
        }
        
        if(((num1>num2)&&(num1<num3))||((num1<num2)&&(num1>num3))){
            mid=num1;
        } else if(((num2>num1)&&(num2<num3))||((num2<num1)&&(num2>num3))){
            mid=num2;
        } else if(((num3>num1)&&(num3<num2))||((num3<num1)&&(num3>num2))){
            mid=num3;
        }
        
        if((num1<num2)&&(num1<num3)){
            min=num1;
        } else if((num2<num1)&&(num2<num3)){
            min=num2;
        } else if((num3<num1)&&(num3<num2)){
            min=num3;
        }
        //</editor-fold>

        if (palo1==palo2){
            //20 asegurados
            tantos=20;
            //Si son las 3 del mismo palo, sumo las mejores
            if(palo2==palo3){
                if((num1<8)&&(num2<8)&&(num3<8)){
                    tantos+=max+mid;
                } else if((num1<8)&&(num2<8)&&(num3>7)){
                    tantos+=num1+num2;
                } else if((num1<8)&&(num2>7)&&(num3<8)){
                    tantos+=num1+num3;
                } else if((num1<8)&&(num2>7)&&(num3>7)){
                    tantos+=num1;
                } else if((num1>7)&&(num2<8)&&(num3<8)){
                    tantos+=num2+num3;
                } else if((num1>7)&&(num2<8)&&(num3>7)){
                    tantos+=num2;
                } else if((num1>7)&&(num2>7)&&(num3<8)){
                    tantos+=num3;
                }
            } else{
                if((num1<8)&&(num2<8)){
                    tantos+=num1+num2;
                } else if((num1<8)&&(num2>7)){
                    tantos+=num1;
                } else if((num1>7)&&(num2<8)){
                    tantos+=num2;
                }
            }
        } else if(palo1==palo3){
            tantos=20;
            if((num1<8)&&(num3<8)){
                tantos+=num1+num3;
            } else if((num1<8)&&(num3>7)){
                tantos+=num1;
            } else if((num1>7)&&(num3<8)){
                tantos+=num3;
            }
        } else if(palo2==palo3){
            tantos=20;
            if((num2<8)&(num3<8)){
                tantos+=num2+num3;
            } else if((num2>7)&&(num3<8)){
                tantos+=num3;
            } else if((num2<8)&&(num3>7)){
                tantos+=num2;
            }
        } else{
            if(max<8){
                tantos=max;
            } else if(mid<8){
                tantos=mid;
            } else if(min<8){
                tantos=min;
            } else{
                tantos=0;
            }
        }
        return tantos;
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
