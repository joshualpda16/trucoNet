/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import formularios.frmJuego;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Joshua
 */
public class Juego {
    private String nombreSala;
    private int instanciaJuego;
    private boolean ronda;
    private int instanciaTruco;
    private int instanciaEnvido;
    private boolean envido=true;//Se puede cantar
    private boolean truco=true;//True es que si se puede cantar
    private int tieneElQuiero;
    private int mano;
    private int turno;
    private Carta cartaInstancia;
    private boolean primeraCarta;
    private String esperando="";

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
        }
    }

    public static void nuevaMano(){
        List<Carta> lst6Cartas=new ArrayList();
            
        lst6Cartas = Juego.crearCartasRandom();

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

    public Carta getCartaInstancia() {
        return cartaInstancia;
    }
    
    public static void ganarMano(int id){
        int puntos=0;
        /*switch(claseGeneral.miJuego.getInstanciaEnvido()){
            case 1:
                puntos=2;
                break;
            case 2:
                puntos=4;
                break;
            case 3:
                puntos=5;
                break;
            case 4:
                puntos=7;
                break;
            case 5:
                if(claseGeneral.lstJugadores.get(Math.abs(id-1)).getPuntos()<15){
                    puntos=15-claseGeneral.lstJugadores.get(Math.abs(id-1)).getPuntos();
                } else if(claseGeneral.lstJugadores.get(Math.abs(id-1)).getPuntos()>15){
                    puntos=30-claseGeneral.lstJugadores.get(Math.abs(id-1)).getPuntos();
                }
                break;
        }*/
        
        if(claseGeneral.miJuego.isTruco()){
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
        }
        
        claseGeneral.lstJugadores.get(id).setPuntos(claseGeneral.lstJugadores.get(id).getPuntos()+puntos);
        frmJuego.txtYo.setText(""+claseGeneral.lstJugadores.get(claseGeneral.getMiId()).getPuntos());
        frmJuego.txtEl.setText(""+claseGeneral.lstJugadores.get(Math.abs(claseGeneral.getMiId()-1)).getPuntos());
        
        claseGeneral.miJuego.setRonda(false);
        
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setInstanciasGanadas(0);
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setCartasTiradas(0);
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setCarta1tirada(false);
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setCarta2tirada(false);
        claseGeneral.lstJugadores.get(claseGeneral.getMiId()).setCarta3tirada(false);
        
        claseGeneral.lstJugadores.get(Math.abs(claseGeneral.getMiId()-1)).setInstanciasGanadas(0);
        claseGeneral.lstJugadores.get(Math.abs(claseGeneral.getMiId()-1)).setCartasTiradas(0);
        
        if(claseGeneral.isSoyServer()){
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
    
    
    //Gets y Sets
    public void setCartaInstancia(Carta cartaInstancia) {
        this.cartaInstancia = cartaInstancia;
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
}
