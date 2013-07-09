package clases;

import formularios.frmPrincipal;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SimpleServer {

	private ServerSocket ss;
	private int portNumber;
	private AcceptingThread acceptingThread;
	private static final String ENCODING = "ISO-8859-1";
        static ObjectOutputStream salida;
        static ObjectInputStream entrada;
        private Socket s;

	public SimpleServer(int portNumber) {
		this.portNumber = portNumber;
		this.ss = null;
		this.acceptingThread = null;
	}

	public void run() {
		log("[Server] Creación de sala...");
		if (this.acceptingThread == null) {
			this.acceptingThread = new AcceptingThread();
			this.acceptingThread.start(); // *
		}
	}

	public void stop() throws IOException {
		log("[Server] stop requested...");
		if (this.acceptingThread != null) {
			this.acceptingThread.abort();
		}
	}
        
        //Envia el mensaje al cliente
        public static void enviarDatos(String mensaje){
            try{ //Envía objeto al cliente
                salida.writeObject(mensaje);
                salida.flush(); //envía toda la salida al cliente
            } catch(IOException exepcionES){
                frmPrincipal.log("\nError al enviar datos");
            }
        }

	private void processConnection() throws IOException {
                salida = new ObjectOutputStream(s.getOutputStream()); //Flujo de salida hacia el cliente
                salida.flush();
                
                entrada = new ObjectInputStream(s.getInputStream()); //Flujo de entrada del cliente
                frmPrincipal.log("[Server] Se obtuvieron los flujos de E/S");
                String mensaje="";
                
                do{ //procesa los mensajes enviados desde el cliente
                    try{ //Lee el mensaje
                        mensaje = (String) entrada.readObject(); //Lee el nuevo mensaje
                        claseGeneral.procesarMensaje(mensaje);
                    } catch (ClassNotFoundException exceptionClaseNoEncontrada){
                        frmPrincipal.log("\nSe recibió un tipo de objeto desconocido:\n\tmensaje: "+mensaje);
                    }
                } while(!mensaje.equals("CNXOUT0"));
	}

	private void log(String s) {
		frmPrincipal.log(s);
	}

	/**
	 * Clase interna (inner class) que implementa la funcionalidad necesaria para abrir un thread nuevo en donde quedarse escuchando los coneciones entrantes.
	 */
	private class AcceptingThread extends Thread {
            private boolean shutdownRequested = false;
            private ServerSocket ss = null;

            // Dentro de este run (que es invocado internamente en el start*) tiene el codigo para levantar el servidor
            public void run() {
                try {
                    log("[AcceptingThread] Iniciando... ");
                    this.ss = new ServerSocket(portNumber); // Reservo el puerto deseado y se permite el ingresar a conexiones, es decir.. se levanta un servidor de sockets :)
                    try {
                        while (!shutdownRequested) {
//                            Socket s = null;
                            try {
                                log("[AcceptingThread] Aceptando conexiones en el puerto: " + this.ss.getLocalPort());
                                s = this.ss.accept(); // se levanta una conexion de la cola, en la variable "s" tengo la conexion o "enchufe" (traduccion de socket por google)
                                log("[AcceptingThread] Conexión recibida desde " + s.getInetAddress().toString());
                                processConnection(); // se la procesa invocando el método de la clase exterior (SimpleServer) que se llama processConnection
                            } catch (SocketException se) {
                                log("[AcceptingThread] Terminada - "
                                        + se.getMessage()); // por aca pasa cuando se TERMINA "manualmente" (es decir, libero el puerto y dejo de escuchar conexiones entrantes)
                            } finally {
                                if (s != null) {
                                    s.close(); // por las dudas cierro la conexion.. nunca viene mal
                                }
                            }
                        }
                    } catch (Exception e) {
                        log("[AcceptingThread] Exception - " + e.getMessage());
                    } finally {
                        if (this.ss != null) {
                            this.ss.close(); // lo mismo aca, pero aca cierro el server (el que escucha y levanta conexiones)
                        }
                    }
                } catch (IOException e) {
                    log("[AcceptingThread] IOException at start - " + e.getMessage()); // como que no deberia pasar
                } finally {
                    log("[AcceptingThread] Stoped. "); // solamente logeo que si estoy aca entonces no estoy escuchando
                }
            }

        // dentro de este m�tedo que se ejecuta DIRECTAMENTE (es decir, como programadores estamos escribiendo su invocacion en algun momento del programa) se baja al servidor
            public void abort() throws IOException {
                this.shutdownRequested = true;
                if (this.ss != null) {
                    this.ss.close();
            }
        }
    }

}
