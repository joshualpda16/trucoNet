/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author Joshua
 */
public class Carta {
    private char palo;
    private int numero;
    private int valor;

    public Carta(char palo, int numero) {
        this.palo = palo;
        this.numero = numero;
        this.valor=calcularValor();
    }
    
    public String traerCarta(){
        if(numero<=9){
            return ""+palo+"0"+numero;
        } else{
            return ""+palo+numero;
        }
    }
    
    private int calcularValor(){
        int val=0;
        switch(this.numero){
            //<editor-fold defaultstate="collapsed" desc="Consigue valor según numero y palo">
            case 1:
                //<editor-fold defaultstate="collapsed" desc="Según Palo">
                switch(this.palo){
                    case 'E':
                        val=14;
                        break;
                    case 'B':
                        val=13;
                        break;
                    case 'O':
                    case 'C':
                        val=8;
                        break;
                }
                break;
                //</editor-fold>
            case 2:
                val=9;
                break;
            case 3:
                val=10;
                break;
            case 4:
                val=1;
                break;
            case 5:
                val=2;
                break;
            case 6:
                val=3;
                break;
            case 7:
                //<editor-fold defaultstate="collapsed" desc="Según Palo">
                switch(palo){
                    case 'E':
                        val=12;
                        break;
                    case 'O':
                        val=11;
                        break;
                    case 'C':
                    case 'B':
                        val=4;
                        break;
                }
                break;
                //</editor-fold>
            case 10:
                val=5;
                break;
            case 11:
                val=6;
                break;
            case 12:
                val=7;
                break;
                //</editor-fold>
        }
        return val;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Gets y Sets">
    public char getPalo() {
        return palo;
    }
    
    public int getNumero() {
        return numero;
    }
    
    public int getValor() {
        return valor;
    }
    
    public void setPalo(char palo) {
        this.palo = palo;
    }
    
    public void setNumero(int numero) {
        this.numero = numero;
    }
    //</editor-fold>
}
