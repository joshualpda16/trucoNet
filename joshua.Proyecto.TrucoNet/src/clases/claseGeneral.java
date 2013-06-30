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
    
    //Declaración de variables
    private static final claseGeneral INSTANCE = new claseGeneral();
    private SimpleServer simpleServer;
    private SimpleClient simpleClient;
    private String nombreSala;
    private int puertoSala;
    private boolean svActivo;
    
    
    //<editor-fold defaultstate="collapsed" desc="Abrir y cerrar JFrames">
    public void mostrarCrearSala(){
        frmPrincipal.jDesktopPane1.add(formCrearSala);
        formCrearSala.setLocation(10, 10);
        formCrearSala.show();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Metodos de conexión">
    public void crearSala(String nombre, int puerto){
        this.nombreSala=nombre;
        this.puertoSala=puerto;
        
        simpleServer = new SimpleServer(puerto);
        simpleServer.run();
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
        try {
            simpleClient.connect();
        } catch (IOException e) {
            log("IOException in sendMessage(): " + e.getMessage());
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="log, Constructor y getInstance">
    private claseGeneral(){
        formCrearSala = new frmCrearSala();
    }
    
    public void log(String msj){
        frmPrincipal.txtLog.append(msj+"\n");
    }
    
    public static claseGeneral getInstance(){
        return INSTANCE;
    }
    //</editor-fold>
}
