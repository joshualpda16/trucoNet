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
    private frmCrearSala formCrearSala;
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
                switch(msj){
                    case "CCTS":
                        //Recibo mis cartas
                        String ctas1 = mensaje.substring(7);
                        lstJugadores.get(1).guardarStringCartas(ctas1);
                        break;
                    case "SCTS":
                        //Recibo cartas del servidor
                        String ctas2 = mensaje.substring(7);
                        lstJugadores.get(0).guardarStringCartas(ctas2);
                        abrirJuego();
                        miJuego = new Juego();
                        break;
                }
                break;
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
