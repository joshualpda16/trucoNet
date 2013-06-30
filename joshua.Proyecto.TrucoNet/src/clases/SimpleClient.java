package clases;

import formularios.frmPrincipal;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SimpleClient {

    private String hostname;
    private String mensaje;
    private int port;
    private Socket socket;
    private AcceptingThread acceptingThread;
    static ObjectOutputStream salida;
    static ObjectInputStream entrada;
    

    public SimpleClient(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
        this.acceptingThread=null;
    }
    
    public void run(){
        log("[Cliente] Conexión a sala...");
        if(this.acceptingThread==null){
            this.acceptingThread = new AcceptingThread();
            this.acceptingThread.start();
        }
    }

    public static void enviarDatos(String mensaje){
        try{
            salida.writeObject(mensaje);
            salida.flush();
        } catch(IOException excepcionES){
            frmPrincipal.log("Excepcion E/S");
        }
    }
    
    
    private class AcceptingThread extends Thread {
        private boolean shutdownRequested = false;
        private Socket sock = null;

        // Dentro de este run (que es invocado internamente en el start*) tiene el codigo para levantar el servidor
        public void run() {
            try {
                //Conectar
                log("[Cliente] Conectando... ");
                this.sock = new Socket(hostname,port);
                log("[Cliente] Conexión exitosa");
                
                //Obtener flujos
                salida = new ObjectOutputStream(sock.getOutputStream());
                salida.flush();
                entrada = new ObjectInputStream(sock.getInputStream());
                log("Se obtuvieron los flujos de E/S");
                
                //Notificar al servidor
                enviarDatos("CNXCNCL");
                
                //Dejo procesando la conexión
                procesarConexion();
            } catch (IOException e) {
                log("[AcceptingThread] IOException at start - " + e.getMessage()); // como que no deberia pasar
            } finally {
                log("[AcceptingThread] Stoped. "); // solamente logeo que si estoy aca entonces no estoy escuchando
            }
        }
    }
    
    private void procesarConexion() throws IOException{
        do{
            try{
                mensaje = (String) entrada.readObject();
                claseGeneral.procesarMensaje(mensaje);
            } catch(ClassNotFoundException excepcionClaseNoEncontrada){
                log("Se recibió un tipo de objeto desconocido");
            }
        } while(!mensaje.equals("Fin"));
    }
    
    public void close () throws IOException {
    	if (this.socket != null) {
            this.socket.close();
    	}
    }

    private void log (String s) {
        frmPrincipal.log(s);
    }
}