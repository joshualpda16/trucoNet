/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Joshua
 */
public class Juego {
    private String nombreSala;

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }
    
    public static List separarCartas(List cartas,int mitad){
        List<Carta> lstCartas=new ArrayList();
        int x=0;
        if(mitad==2) {
            x=3;
        }
        int fin=3+x;
        while(x!=fin){
            lstCartas.add((Carta) cartas.get(x));
            x++;
        }
        return lstCartas;
    }
    
    public static List crearCartasRandom(){
        List<Carta> lstCartas=new ArrayList();
        boolean repetido=false;
        int x=0;
        while(x<=5){
            repetido=false;
            
            //<editor-fold defaultstate="collapsed" desc="Creo el Palo">
            Random rand = new Random();
            int codPalo= rand.nextInt(4);
            codPalo++;
            char palo='Z';
            switch(codPalo){
                case 1:
                    palo='O';
                    break;
                case 2:
                    palo='C';
                    break;
                case 3:
                    palo='B';
                    break;
                case 4:
                    palo='E';
                    break;
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Creo el Número">
            Random rand1 = new Random();
            int codNumero=(rand1.nextInt(4))+1;
            int numero;
            switch(codNumero){
                case 8:
                    numero=10;
                    break;
                case 9:
                    numero=11;
                    break;
                case 10:
                    numero=12;
                    break;
                default:
                    numero=codNumero;
                    break;
            }
            //</editor-fold>
            Carta carta = new Carta(palo,numero);
            
            int cantCartas = lstCartas.size();
            if(x!=0){
                for(int z=0;z<cantCartas;z++){
                    if((lstCartas.get(z).getNumero()==numero)&&(lstCartas.get(z).getPalo()==palo)){
                        repetido=true;
                    }
                }
            } else{
                repetido=false;
            }
            if(!repetido){
                lstCartas.add(carta);
                x++;
            }
        }
        return lstCartas;
    }
}
