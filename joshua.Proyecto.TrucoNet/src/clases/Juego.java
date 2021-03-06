/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import formularios.frmChatPrevio;
import formularios.frmJuego;
import formularios.frmPrincipal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author Joshua
 */
public class Juego {
    
    //<editor-fold defaultstate="collapsed" desc="Atributos">
    private String nombreSala;
    private String esperando="";
    private int instanciaJuego;
    private int instanciaTruco;
    private int instanciaEnvido;
    private int tieneElQuiero;
    private int mano;
    private int turno;
    private int primeraEnCasa;
    private boolean trucoJugando=false;
    private boolean ronda;
    private boolean envido=true;//Se puede cantar
    private boolean truco=true;//True es que si se puede cantar
    private boolean primeraCarta;
    private Carta cartaInstancia;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos">
    public Juego() {
        instanciaJuego=0;
        ronda=true;
        instanciaTruco=0;
        instanciaEnvido=0;
        primeraCarta=true;
        mano=0;
        turno=mano;
    }
    
    public void inicializar(){
        instanciaJuego=0;
        ronda=false;
        instanciaTruco=0;
        instanciaEnvido=0;
        primeraCarta=true;
        mano=Math.abs(mano-1);
        turno=mano;
        envido=true;
        truco=true;
        esperando="";
        trucoJugando=false;
    }
    
    public void pintarCartas(){
        claseGeneral.formJuego.pintar(frmJuego.miCartaTirada1, "Blanco");
        claseGeneral.formJuego.pintar(frmJuego.miCartaTirada2, "Blanco");
        claseGeneral.formJuego.pintar(frmJuego.miCartaTirada3, "Blanco");
        claseGeneral.formJuego.pintar(frmJuego.suCartaTirada1, "Blanco");
        claseGeneral.formJuego.pintar(frmJuego.suCartaTirada2, "Blanco");
        claseGeneral.formJuego.pintar(frmJuego.suCartaTirada3, "Blanco");
        claseGeneral.formJuego.pintarMisCartas();
        claseGeneral.formJuego.pintar(frmJuego.lblYoCanto,"Blanco");
        claseGeneral.formJuego.pintar(frmJuego.lblElCanta,"Blanco");
        claseGeneral.miJuego.setRonda(true);
        frmJuego.cmdAlMazo.setEnabled(true);
        if(claseGeneral.miJuego.getTurno()==claseGeneral.getMiId()){
            claseGeneral.formJuego.prenderBotones();
            frmJuego.cmdQuiero.setText("Quiero");
            frmJuego.cmdNoQuiero.setText("No Quiero");
            frmJuego.cmdQuiero.setEnabled(false);
            frmJuego.cmdNoQuiero.setEnabled(false);
        }
    }
    
    public static void nuevaMano(){
        List<Carta> lst6Cartas=Juego.crearCartasRandom();
        
        claseGeneral.lstJugadores.get(0).setCartas(Juego.separarCartas(lst6Cartas,1));
        claseGeneral.lstJugadores.get(1).setCartas(Juego.separarCartas(lst6Cartas,2));
        
        Jugador j1 = (Jugador) claseGeneral.lstJugadores.get(1);
        Jugador j0 = (Jugador) claseGeneral.lstJugadores.get(0);
        
        SimpleServer.enviarDatos("JGOCCTS"+j1.cartasToString(j1.getCartas()));
        SimpleServer.enviarDatos("JGOSVCT"+j0.cartasToString(j0.getCartas()));
        SimpleServer.enviarDatos("JGONVMN");
        
        claseGeneral.miJuego.inicializar();
        
        if(claseGeneral.miJuego.getMano()!=claseGeneral.getMiId()){
            claseGeneral.formJuego.apagarTodosBotones();
            frmJuego.cmdAlMazo.setText("Repartir");
            frmJuego.cmdAlMazo.setEnabled(true);
            claseGeneral.formJuego.suTurno();
        } else{
            claseGeneral.formJuego.apagarTodosBotones();
            frmJuego.cmdAlMazo.setText("Al Mazo");
            frmJuego.cmdAlMazo.setEnabled(false);
            claseGeneral.formJuego.miTurno();
        }
    }
    
    public static void ganarMano(int id){
        int puntos=0;
        
        switch(claseGeneral.miJuego.getInstanciaTruco()){
            case 0:
                puntos=1;
                break;
            case 2:
                puntos+=2;
                break;
            case 4:
                puntos+=4;
                break;
            case 3:
                puntos+=3;
                break;
        }
        
        claseGeneral.lstJugadores.get(id).setPuntos(claseGeneral.lstJugadores.get(id).getPuntos()+puntos);
        
        claseGeneral.formJuego.actualizarPuntos();
        
        claseGeneral.miJuego.setRonda(false);
        
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setInstanciasGanadas(0);
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setCartasTiradas(0);
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setCarta1tirada(false);
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setCarta2tirada(false);
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setCarta3tirada(false);
        
        claseGeneral.lstJugadores.get(Math.abs(claseGeneral.getMiId()-1)).setInstanciasGanadas(0);
        claseGeneral.lstJugadores.get(Math.abs(claseGeneral.getMiId()-1)).setCartasTiradas(0);
        
        if(claseGeneral.lstJugadores.get(claseGeneral.getMiId()).getPuntos()>=30){
            JOptionPane.showMessageDialog(null, "Felicidades!, ganaste la partida!");
            claseGeneral.formJuego.apagarTodosBotones();
            frmJuego.cmdAlMazo.setText("Revancha!");
            frmJuego.cmdAlMazo.setEnabled(true);
        } else if(claseGeneral.lstJugadores.get(Math.abs(claseGeneral.getMiId()-1)).getPuntos()>=30){
            JOptionPane.showMessageDialog(null, "Que lástima, perdiste la partida!");
            claseGeneral.formJuego.apagarTodosBotones();
            frmJuego.cmdAlMazo.setText("Revancha!");
            frmJuego.cmdAlMazo.setEnabled(true);
        } else if(claseGeneral.isSoyServer()){
            nuevaMano();
        }
    }
    
    public static void metodoRevancha(){
        //Pongo los puntos en 0
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setPuntos(0);
        claseGeneral.lstJugadores.get(Math.abs(claseGeneral.getMiId()-1)).setPuntos(0);
        claseGeneral.formJuego.actualizarPuntos();

        //Aviso que estoy esperando la respuesta
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setBold(attrs, true);
        try {
            frmJuego.txtChat.getStyledDocument().insertString(frmJuego.txtChat.getStyledDocument().getLength(),"Esperando respuesta de revancha!",attrs);
        } catch (BadLocationException ex) {
            Logger.getLogger(claseGeneral.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Pregunto si él me pidió la revancha primero
        if(claseGeneral.miJuego.getEsperando().equals("revancha")){
            //Si ya me pidió la revancha...
            try {
                frmJuego.txtChat.getStyledDocument().insertString(frmJuego.txtChat.getStyledDocument().getLength(),"Revancha aceptada!\nComienza nueva partida",attrs);
            } catch (BadLocationException ex) {
                Logger.getLogger(claseGeneral.class.getName()).log(Level.SEVERE, null, ex);
            }

            if(claseGeneral.isSoyServer()){ 
                SimpleServer.enviarDatos("JGONVJ10"); //Le aviso que arranca la partida
            } else{
                claseGeneral.miJuego.inicializar();
                SimpleClient.enviarDatos("JGONVJ20"); //Le aviso que acepté
            }

            if(claseGeneral.isSoyServer()){ //Si soy el server, solo reparto una nueva mano
                nuevaMano();
            } //Si soy el cliente, espero las cartas nuevas

        //Si soy el primero en pedir la revancha...
        } else{
            //Apago los botones
            frmJuego.cmdAlMazo.setEnabled(false);
            claseGeneral.formJuego.apagarTodosBotones();

            if(claseGeneral.isSoyServer()){ 
                SimpleServer.enviarDatos("JGOREVA"); //Le aviso que quiero la revancha
            } else{
                SimpleClient.enviarDatos("JGOREVA"); //Le aviso que quiero la revancha
            }
            //Espero la revancha
            claseGeneral.miJuego.setEsperando("revancha");
        }
    }
    
    public static void alMazo(int id){
        int puntos=0;
        if(claseGeneral.miJuego.isTruco()&&!claseGeneral.miJuego.isTrucoJugando()&&claseGeneral.lstJugadores.get(Math.abs(id-1)).getCartasTiradas()>0){
            switch(claseGeneral.miJuego.getInstanciaTruco()){
                case 0:
                    puntos=1;
                    break;
                case 2:
                    puntos+=1;
                    break;
                case 4:
                    puntos+=3;
                    break;
                case 3:
                    puntos+=2;
                    break;
            }
            
        } else{
            switch(claseGeneral.miJuego.getInstanciaTruco()){
                case 0:
                    puntos+=1;
                    break;
                case 2:
                    puntos+=2;
                    break;
                case 4:
                    puntos+=4;
                    break;
                case 3:
                    puntos+=3;
                    break;
            }
        }
        
        if((claseGeneral.miJuego.getInstanciaJuego()==0)&&(claseGeneral.miJuego.isEnvido())&&(claseGeneral.miJuego.getInstanciaTruco()==0)&&(claseGeneral.lstJugadores.get(Math.abs(id-1)).getCartasTiradas()==0)){
            puntos++;
        }
        
        claseGeneral.lstJugadores.get(Math.abs(id-1)).setPuntos(claseGeneral.lstJugadores.get(Math.abs(id-1)).getPuntos()+puntos);
        
        claseGeneral.formJuego.actualizarPuntos();
        
        claseGeneral.miJuego.setRonda(false);
        
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setInstanciasGanadas(0);
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setCartasTiradas(0);
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setCarta1tirada(false);
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setCarta2tirada(false);
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setCarta3tirada(false);
        
        claseGeneral.lstJugadores.get(Math.abs(claseGeneral.getMiId()-1)).setInstanciasGanadas(0);
        claseGeneral.lstJugadores.get(Math.abs(claseGeneral.getMiId()-1)).setCartasTiradas(0);
        
        if(claseGeneral.lstJugadores.get(claseGeneral.getMiId()).getPuntos()>=30){
            JOptionPane.showMessageDialog(null, "Felicidades!, ganaste la partida!");
            claseGeneral.formJuego.apagarTodosBotones();
            frmJuego.cmdAlMazo.setText("Revancha!");
            frmJuego.cmdAlMazo.setEnabled(true);
        } else if(claseGeneral.lstJugadores.get(Math.abs(claseGeneral.getMiId()-1)).getPuntos()>=30){
            JOptionPane.showMessageDialog(null, "Que lástima, perdiste la partida!");
            claseGeneral.formJuego.apagarTodosBotones();
            frmJuego.cmdAlMazo.setText("Revancha!");
            frmJuego.cmdAlMazo.setEnabled(true);
        } else if(claseGeneral.isSoyServer()){
            nuevaMano();
        }
    }
    
    public static boolean compararCartas(Carta miCarta, Carta suCarta){
        if(miCarta.getValor()>suCarta.getValor()){
            return true;
        } else{
            return false;
        }
    }
    
    public static boolean mismosPuntos(Carta miCarta, Carta suCarta){
        if(miCarta.getValor()==suCarta.getValor()){
            return true;
        } else{
            return false;
        }
    }
    
    public static void sumarTantos(int id){
        int ptos=0;
        switch(claseGeneral.miJuego.getInstanciaEnvido()){
            case 1:
                ptos=2;
                break;
            case 2:
                ptos=4;
                break;
            case 3:
                ptos=5;
                break;
            case 4:
                ptos=7;
                break;
            case 5:
                int susPuntos=claseGeneral.lstJugadores.get(Math.abs(id-1)).getPuntos();
                if(susPuntos<15){
                    ptos=15-susPuntos;
                } else{
                    ptos=30-susPuntos;
                }
                break;
            case 6:
                ptos=3;
                break;
        }
        
        claseGeneral.lstJugadores.get(id).setPuntos(ptos+claseGeneral.lstJugadores.get(id).getPuntos());
        
        if(claseGeneral.lstJugadores.get(claseGeneral.getMiId()).getPuntos()>=30){
            JOptionPane.showMessageDialog(null, "Felicidades!, ganaste la partida!");
            claseGeneral.formJuego.apagarTodosBotones();
            frmJuego.cmdAlMazo.setText("Revancha!");
            frmJuego.cmdAlMazo.setEnabled(true);
        } else if(claseGeneral.lstJugadores.get(Math.abs(claseGeneral.getMiId()-1)).getPuntos()>=30){
            JOptionPane.showMessageDialog(null, "Que lástima, perdiste la partida!");
            claseGeneral.formJuego.apagarTodosBotones();
            frmJuego.cmdAlMazo.setText("Revancha!");
            frmJuego.cmdAlMazo.setEnabled(true);
        } else if(claseGeneral.isSoyServer()){
            nuevaMano();
        }
        
        claseGeneral.formJuego.actualizarPuntos();
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
            int codNumero=(rand1.nextInt(9))+1;
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Gets y Sets">
    public Carta getCartaInstancia() {
        return cartaInstancia;
    }
    
    public void setCartaInstancia(Carta cartaInstancia) {
        this.cartaInstancia = cartaInstancia;
    }
    
    public int getPrimeraEnCasa() {
        return primeraEnCasa;
    }
    
    public void setPrimeraEnCasa(int primeraEnCasa) {
        this.primeraEnCasa = primeraEnCasa;
    }
    
    public boolean isTrucoJugando() {
        return trucoJugando;
    }
    
    public void setTrucoJugando(boolean trucoJugando) {
        this.trucoJugando = trucoJugando;
    }
    
    public int getTieneElQuiero() {
        return tieneElQuiero;
    }
    
    public void setTieneElQuiero(int tieneElQuiero) {
        this.tieneElQuiero = tieneElQuiero;
    }
    
    public String getEsperando() {
        return esperando;
    }
    
    public void setEsperando(String esperando) {
        this.esperando = esperando;
    }
    
    public boolean isPrimeraCarta() {
        return primeraCarta;
    }
    
    public void setPrimeraCarta(boolean primeraCarta) {
        this.primeraCarta = primeraCarta;
    }
    
    public void setTurno(int turno) {
        this.turno = turno;
    }
    
    public int getInstanciaJuego() {
        return instanciaJuego;
    }
    
    public void setInstanciaJuego(int instanciaJuego) {
        this.instanciaJuego = instanciaJuego;
    }
    
    public boolean isRonda() {
        return ronda;
    }
    
    public void setRonda(boolean ronda) {
        this.ronda = ronda;
    }
    
    public int getInstanciaTruco() {
        return instanciaTruco;
    }
    
    public void setInstanciaTruco(int instanciaTruco) {
        this.instanciaTruco = instanciaTruco;
    }
    
    public int getInstanciaEnvido() {
        return instanciaEnvido;
    }
    
    public void setInstanciaEnvido(int instanciaEnvido) {
        this.instanciaEnvido = instanciaEnvido;
    }
    
    public boolean isEnvido() {
        return envido;
    }
    
    public void setEnvido(boolean envido) {
        this.envido = envido;
    }
    
    public boolean isTruco() {
        return truco;
    }
    
    public void setTruco(boolean truco) {
        this.truco = truco;
    }
    
    public int getTurno() {
        return turno;
    }
    
    public int getMano() {
        return mano;
    }
    
    public String getNombreSala() {
        return nombreSala;
    }
    
    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }
    //</editor-fold>
}
