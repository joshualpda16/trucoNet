/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import formularios.*;
import java.io.IOException;

/**
 *
 * @author Joshua
 */
public class claseGeneral {
    //Formularios
    private frmCrearSala formCrearSala;
    private frmUnirse formUnirse;
    
    //Declaración de variables
    private static final claseGeneral INSTANCE = new claseGeneral();
    static SimpleServer simpleServer;
    static SimpleClient simpleClient;
    static String nombreSala;
    private int puertoSala;
    private boolean svActivo;
    
    public static void procesarMensaje(String mensaje){
        String tipo = mensaje.substring(0,3);
        String msj = mensaje.substring(3,7);
        
        switch(tipo){
            case "CFG":
                if(msj.equals("NSLA")){
                    frmPrincipal.log("Nombre de la sala: "+mensaje.substring(7));
                }
                break;
            case "CNX":
                if(msj.equals("CNCL")){
                    frmPrincipal.log("Cliente conectado correctamente");
                    SimpleServer.enviarDatos("CFGNSLA"+claseGeneral.nombreSala);
                }
                break;
            case "JGO":
                break;
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Abrir y cerrar JFrames">
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Metodos de conexión">
    public void crearSala(String nombre, int puerto){
        this.nombreSala=nombre;
        this.puertoSala=puerto;
        
        simpleServer = new SimpleServer(puerto);
        simpleServer.run();
        
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
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="log, Constructor y getInstance">
    private claseGeneral(){
        formCrearSala = new frmCrearSala();
        formUnirse = new frmUnirse();
    }
    
    public void log(String msj){
        frmPrincipal.log(msj);
    }
    
    public static claseGeneral getInstance(){
        return INSTANCE;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Gets">
    public boolean isSvActivo() {
        return svActivo;
    }
    //</editor-fold>

}
