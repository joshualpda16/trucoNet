/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import formularios.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
public class claseGeneral {
    private static final claseGeneral INSTANCE = new claseGeneral();
    
    //Mis datos
    static String miNombre;
    static int miId;
    static String salaActual;
    static boolean soyServer;
    static public Juego miJuego;
    
    //Juego
    static public List<Jugador> lstJugadores=new ArrayList();
    
    //<editor-fold defaultstate="collapsed" desc="Variables de Formulario">
    static frmCrearSala formCrearSala;
    private frmUnirse formUnirse;
    private frmOpciones formOpciones;
    static frmChatPrevio formChatPrevio;
    static frmJuego formJuego;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Variables de conexión">
    static public SimpleServer simpleServer;
    static public SimpleClient simpleClient;
    private int puertoSala;
    static String nombreSala;
    //</editor-fold>
    
    
    private boolean svActivo;
    
    public static void procesarMensaje(String mensaje){
        String tipo = mensaje.substring(0,3);
        String msj = mensaje.substring(3,7);
        
        switch(tipo){
            case "CFG":
                //<editor-fold defaultstate="collapsed" desc="Mensajes de CFG">
                switch (msj) {
                    case "NSLA":
                        //Recibo nombre de la sala
                        salaActual=mensaje.substring(7);
                        break;
                    case "NVID":
                        //Recibo mi nuevo id como jugador
                        miId=Integer.parseInt(mensaje.substring(7));
                        SimpleClient.enviarDatos("CFGNCLI"+miNombre);
                        break;
                    case "NCLI":
                        //Recibo el nombre del jugador cliente
                        lstJugadores.add(new Jugador(miNombre,0));
                        lstJugadores.add(new Jugador(mensaje.substring(7),1));
                        miId=0;
                        SimpleServer.enviarDatos("CFGNSVR"+miNombre);
                        mostrarChatPrevio();
                        break;
                    case "NSVR":
                        //Recibo el nombre del jugador servidor
                        lstJugadores.add(new Jugador(mensaje.substring(7),0));
                        lstJugadores.add(new Jugador(miNombre,miId));
                        mostrarChatPrevio();
                        break;
                }
                //</editor-fold>
                break;
            case "CNX":
                //<editor-fold defaultstate="collapsed" desc="Mensajes de CNX">
                if(msj.equals("CNCL")){
                    frmPrincipal.log("Cliente conectado correctamente");
                    SimpleServer.enviarDatos("CFGNSLA"+nombreSala);
                    salaActual=nombreSala;
                    SimpleServer.enviarDatos("CFGNVID"+1);
                    formCrearSala.dispose();
                }
                //</editor-fold>
                break;
            case "CHP": //CHP<ID><Mensaje> - CHP1Hola
                //<editor-fold defaultstate="collapsed" desc="Mensajes de CHP">
                if(mensaje.equals("CHP1ready")){ //CHP0ready significa que el cliente está listo
                    frmChatPrevio.cmdListo.setEnabled(true);
                    SimpleAttributeSet attrs = new SimpleAttributeSet();
                    StyleConstants.setBold(attrs, true);
                    try {
                        frmChatPrevio.txtChatPrevio.getStyledDocument().insertString(frmChatPrevio.txtChatPrevio.getStyledDocument().getLength(),"El cliente "+lstJugadores.get(1).getNombre()+" está listo.\n",attrs);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(claseGeneral.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if(mensaje.equals("CHP0ready")){ //CHP0ready es el servidor listo
                    SimpleAttributeSet attrs = new SimpleAttributeSet();
                    StyleConstants.setBold(attrs, true);
                    try {
                        frmChatPrevio.txtChatPrevio.getStyledDocument().insertString(frmChatPrevio.txtChatPrevio.getStyledDocument().getLength(),lstJugadores.get(1).getNombre()+" está listo.\nEsperando inicio del juego\n",attrs);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(claseGeneral.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else{
                    //El nombre primero en negrita
                    SimpleAttributeSet attrs = new SimpleAttributeSet();
                    try {
                        //El nombre primero en negrita
                        StyleConstants.setBold(attrs, true);
                        int indice = Integer.parseInt(mensaje.substring(3,4));
                        frmChatPrevio.txtChatPrevio.getStyledDocument().insertString(frmChatPrevio.txtChatPrevio.getStyledDocument().getLength(),claseGeneral.lstJugadores.get(indice).getNombre() +" > ",attrs);
                        StyleConstants.setBold(attrs, false);
                        frmChatPrevio.txtChatPrevio.getStyledDocument().insertString(frmChatPrevio.txtChatPrevio.getStyledDocument().getLength(),mensaje.substring(4)+"\n",attrs);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(claseGeneral.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                //</editor-fold>
            case "JGO":
                //<editor-fold defaultstate="collapsed" desc="Juego">
                switch(msj){
                    case "CHAT":
                        int suId=Integer.parseInt(mensaje.substring(7,8));
                        
                        SimpleAttributeSet attrs = new SimpleAttributeSet();
                        StyleConstants.setBold(attrs, true);
                        try {
                            //El nombre primero en negrita
                            frmJuego.txtChat.getStyledDocument().insertString(frmJuego.txtChat.getStyledDocument().getLength(),lstJugadores.get(suId).getNombre()+" > ",attrs);
                            StyleConstants.setBold(attrs, false);
                            frmJuego.txtChat.getStyledDocument().insertString(frmJuego.txtChat.getStyledDocument().getLength(),mensaje.substring(8) +"\n",attrs);
                        } catch (BadLocationException ex) {
                            Logger.getLogger(claseGeneral.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "SISI":
                        frmPrincipal.log("Recibí el Quiero");
                        formJuego.pintar(frmJuego.lblElCanta, "Quiero");
                        switch(miJuego.getEsperando()){
                            case "truco":
                                miJuego.setInstanciaTruco(2);
                                miJuego.setRonda(true);
                                miJuego.setEnvido(true);
                                break;
                            case "retruco":
                                miJuego.setInstanciaTruco(3);
                                miJuego.setRonda(true);
                                miJuego.setEnvido(true);
                                break;
                            case "valecuatro":
                                miJuego.setInstanciaTruco(4);
                                miJuego.setRonda(true);
                                miJuego.setEnvido(true);
                                break;
                        }
                        break;
                    case "NONO":
                        frmPrincipal.log("No quiso");
                        break;
                    case "CCTS":
                        //Recibo mis cartas
                        String ctas1 = mensaje.substring(7);
                        lstJugadores.get(miId).guardarStringCartas(ctas1);
                        break;
                    case "SVCT":
                        //Recibo cartas del servidor
                        String ctas3 = mensaje.substring(7);
                        lstJugadores.get(0).guardarStringCartas(ctas3);
                        break;
                    case "NVMN":
                        //Nueva mano
                        miJuego.inicializar();
                        if(miJuego.getMano()!=miId){
                            formJuego.apagarTodosBotones();
                            frmJuego.cmdAlMazo.setText("Repartir");
                            formJuego.suTurno();
                        } else{
                            frmJuego.cmdAlMazo.setText("Al Mazo");
                            formJuego.apagarTodosBotones();
                            claseGeneral.formJuego.miTurno();
                        }
                        break;
                    case "REPA":
                        //Repartir. Pinto las cartas solamente.
                        miJuego.pintarCartas();
                        break;
                    case "SCTS":
                        //Recibo cartas del servidor
                        String ctas2 = mensaje.substring(7);
                        lstJugadores.get(0).guardarStringCartas(ctas2);
                        abrirJuego();
                        miJuego = new Juego();
                        break;
                    case "CRTA": //El contrincante tiró una carta.
                        //<editor-fold defaultstate="collapsed" desc="Llegó una carta">
                        //Traigo la carta y el jugador
                        frmPrincipal.log("Llegó la carta");
                        int suID=Integer.parseInt(mensaje.substring(7, 8));
                        int idCarta=Integer.parseInt(mensaje.substring(8,9));
                        String carta = lstJugadores.get(suID).getCartas().get(idCarta).traerCarta();
                        int susCartasTiradas = lstJugadores.get(suID).getCartasTiradas();
                        boolean yoGano=false,yoPierdo=false;
                        
                        switch(susCartasTiradas){
                            case 0:
                                formJuego.pintar(frmJuego.suCartaTirada1, carta);
                                formJuego.pintar(frmJuego.suCarta1, "Blanco");
                                break;
                            case 1:
                                formJuego.pintar(frmJuego.suCartaTirada2, carta);
                                formJuego.pintar(frmJuego.suCarta2, "Blanco");
                                break;
                            case 2:
                                formJuego.pintar(frmJuego.suCartaTirada3, carta);
                                formJuego.pintar(frmJuego.suCarta3, "Blanco");
                                break;
                        }
                        if(miJuego.isPrimeraCarta()){
                            frmPrincipal.log("Es la primera carta");
                            miJuego.setCartaInstancia(lstJugadores.get(suID).getCartas().get(idCarta));
                            miJuego.setPrimeraCarta(false);
                            formJuego.miTurno();
                            frmPrincipal.log("Me toca a mi");
                        } else{
                            frmPrincipal.log("No es la primera carta");
                            if(Juego.compararCartas(miJuego.getCartaInstancia(),lstJugadores.get(suID).getCartas().get(idCarta))){
                                frmPrincipal.log("Mi carta es más grande");
                                formJuego.miTurno();
                                miJuego.setPrimeraCarta(true);
                                frmPrincipal.log("Mi turno");
                                formJuego.apagarTodosBotones();
                                lstJugadores.get(miId).setInstanciasGanadas(lstJugadores.get(miId).getInstanciasGanadas()+1);
                                
                                if(lstJugadores.get(miId).getInstanciasGanadas()==2){
                                    yoGano=true;
                                }
                            } else{
                                frmPrincipal.log("Mi carta es más chica");
                                formJuego.suTurno();
                                lstJugadores.get(suID).setInstanciasGanadas(lstJugadores.get(suID).getInstanciasGanadas()+1);
                                formJuego.apagarTodosBotones();
                                miJuego.setPrimeraCarta(true);
                                frmPrincipal.log("Su turno");
                                if(lstJugadores.get(suID).getInstanciasGanadas()==2){
                                    yoPierdo=true;
                                }
                            }
                            miJuego.setInstanciaJuego(miJuego.getInstanciaJuego()+1);
                            frmPrincipal.log("Suma una instancia al juego. Estamos en la "+(miJuego.getInstanciaJuego()));
                        }
                        formJuego.prenderBotones();
                        frmPrincipal.log("Sus cartas tiradas son "+susCartasTiradas+1);
                        lstJugadores.get(suID).setCartasTiradas(susCartasTiradas+1);
                        if(yoGano){
                            frmPrincipal.log("Gané dos instancias");
                            Juego.ganarMano(miId);
                        }
                        if(yoPierdo){
                            frmPrincipal.log("Ganó dos instancias");
                            Juego.ganarMano(suID);
                        }
                        //</editor-fold>
                        break;
                    case "TRC1": //El contrincante cantó truco
                        formJuego.pintar(frmJuego.lblElCanta, "Truco");
                        frmJuego.cmdQuiero.setEnabled(true);
                        frmJuego.cmdNoQuiero.setEnabled(true);
                        frmJuego.cmdTruco.setText("Quiero Re Truco");
                        frmJuego.cmdTruco.setEnabled(true);
                        miJuego.setEsperando("truco");
                        break;
                    case "TRC2": //El contrincante cantó Re Truco
                        formJuego.pintar(frmJuego.lblElCanta,"ReTruco");
                        formJuego.pintar(frmJuego.lblYoCanto,"Blanco");
                        frmJuego.cmdQuiero.setEnabled(true);
                        frmJuego.cmdNoQuiero.setEnabled(true);
                        frmJuego.cmdTruco.setText("Quiero vale 4");
                        frmJuego.cmdTruco.setEnabled(true);
                        miJuego.setEsperando("retruco");
                        miJuego.setRonda(false);
                        break;
                    case "TRC3":
                        formJuego.pintar(frmJuego.lblElCanta,"ValeCuatro");
                        formJuego.pintar(frmJuego.lblYoCanto,"Blanco");
                        frmJuego.cmdQuiero.setEnabled(true);
                        frmJuego.cmdNoQuiero.setEnabled(true);
                        frmJuego.cmdTruco.setEnabled(false);
                        miJuego.setEsperando("valecuatro");
                        miJuego.setRonda(false);
                        //</editor-fold>
                }
                break;
        }
    }
    
    public static void enviarQuiero(){
        if(claseGeneral.isSoyServer()){
            SimpleServer.enviarDatos("JGOSISI");
        } else{
            SimpleClient.enviarDatos("JGOSISI");
        }
        miJuego.setRonda(true);
        formJuego.pintar(frmJuego.lblYoCanto, "Quiero");
    }
    
     public static void enviarNoQuiero(){
        if(claseGeneral.isSoyServer()){
            SimpleServer.enviarDatos("JGONONO");
        } else{
            SimpleClient.enviarDatos("JGONONO");
        }
    }
    
    public void guardarNombre(String nombre){
        //Escritura
        File f;
        String ruta="src/otros/config.cfg";
        f = new File(ruta);
        try{
            FileWriter w = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);	
            wr.write(nombre);
            wr.close();
            bw.close();
        }catch(IOException e){};
        verificarConfig();
    }
    
    public void verificarConfig(){
        String ruta = "src/otros/config.cfg";
        String estado = estadoConfig(ruta);
        if(estado.equals("existe")){
            //<editor-fold defaultstate="collapsed" desc="Si existe">
            File fich = null;
            FileReader fichr = null;
            BufferedReader bf = null;
            
            try
            {
                // Apertura del fichero y creacion de BufferedReader
                // para hacer lectura con readLine()
                fich = new File (ruta);
                fichr = new FileReader (fich);
                bf = new BufferedReader(fichr);
                
                // Lectura del fichero y escritura en pantalla
                String line;
                while((line=bf.readLine())!=null){
                    miNombre=line;
                    log("Su nombre es: "+line);
                }
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                // En el finally cerramos el fichero con
                // control de excepcions
                try
                {
                    if( bf != null )
                    {
                        bf.close();
                    }
                }
                catch (Exception e2)
                {
                    e2.printStackTrace();
                }
            }
            //</editor-fold>
        } else if(estado.equals("noexiste")){
            //<editor-fold defaultstate="collapsed" desc="Si no existe">
            int seleccion = JOptionPane.showOptionDialog(null, "El archivo de configuración no existe, se creará uno nuevo.\nDesea configurar su información personal ahora?",
                    "No existe",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,new Object[]{"Si","No"},
                    null);
            //Escritura
            try{
                File f;
                f = new File("src/otros/config.cfg");
                FileWriter w = new FileWriter(f);
                BufferedWriter bw = new BufferedWriter(w);
                PrintWriter wr = new PrintWriter(bw);
                wr.write("Anonimo");
                wr.close();
                bw.close();
                miNombre="Anonimo";
            }catch(IOException e){
            };
            if(seleccion<0){
                mostrarOpciones();
            } else{
                
            }
            //</editor-fold>
        } else{
            JOptionPane.showMessageDialog(null, estado);
        }
    }
    
    static String estadoConfig(String ruta){
        String msj="";
        File archivoConfig = new File(ruta);
        if(!archivoConfig.exists()){
            msj="noexiste";
        } else if(!archivoConfig.canRead()){
            msj="El archivo de configuración no se puede leer!";
        } else if(!archivoConfig.canWrite()){
            msj="El archivo de configuración es de solo lectura!\nNo podrá guardar información nueva";
        } else{
            msj="existe";
        }
        return msj;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Abrir y cerrar JFrames">
    static public void abrirJuego(){
        formChatPrevio.dispose();
        frmPrincipal.jDesktopPane1.add(formJuego);
        formJuego.setLocation(10,10);
        formJuego.show();
        formJuego.pintarMisCartas();
    }
    
    public void mostrarCrearSala(){
        frmPrincipal.jDesktopPane1.add(formCrearSala);
        formCrearSala.setLocation(10, 10);
        formCrearSala.show();
    }
    
    public void mostrarUnirse(){
        frmPrincipal.jDesktopPane1.add(formUnirse);
        formUnirse.setLocation(10,10);
        formUnirse.show();
    }
    
    public void mostrarOpciones(){
        frmPrincipal.jDesktopPane1.add(formOpciones);
        formOpciones.setLocation(10,10);
        formOpciones.show();
    }
    
    static void mostrarChatPrevio(){
        frmPrincipal.jDesktopPane1.add(formChatPrevio);
        formChatPrevio.show();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Metodos de conexión">
    public void crearSala(String nombre, int puerto){
        nombreSala=nombre;
        puertoSala=puerto;
        
        simpleServer = new SimpleServer(puerto);
        simpleServer.run();
        
        soyServer=true;
        this.svActivo=true;
    }
    
    public void cerrarSala(){
        try {
            simpleServer.stop();
        } catch (IOException e) {
            log("IOException in closeServer(): " + e.getMessage());
        }
    }
    
    public void conectarAlServidor(String host, int port){
        simpleClient = new SimpleClient(host, port);
        simpleClient.run();
        soyServer=false;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="log, Constructor y getInstance">
    private claseGeneral(){
        formCrearSala = new frmCrearSala();
        formUnirse = new frmUnirse();
        formOpciones = new frmOpciones();
        formChatPrevio = new frmChatPrevio();
        formJuego = new frmJuego();
    }
    
    public void log(String msj){
        frmPrincipal.log(msj);
    }
    
    public static claseGeneral getInstance(){
        return INSTANCE;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="GetsSets">

    public static boolean isSoyServer() {
        return soyServer;
    }

    public static void setSoyServer(boolean soyServer) {
        claseGeneral.soyServer = soyServer;
    }
    
    public static String getMiNombre() {
        return miNombre;
    }

    public static int getMiId() {
        return miId;
    }
    
    public static String getNombreSala(){
        return salaActual;
    }

    public static void setMiNombre(String miNombre) {
        claseGeneral.miNombre = miNombre;
    }
    
    public boolean isSvActivo() {
        return svActivo;
    }
    //</editor-fold>

}
