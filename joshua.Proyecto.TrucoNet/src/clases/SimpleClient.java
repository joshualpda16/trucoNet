package clases;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleClient {

    private String hostname;
    private int port;
    private Socket socket;

    public SimpleClient(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    public void connect() throws UnknownHostException, IOException{
        log("[Client] - Attempting to connect to "+hostname+":"+port);
        socket = new Socket(hostname,port);
        log("[Client] - Connection Established");
    }

    public void send(String message) throws IOException{
    	OutputStream os = socket.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os)); // operaciones par abrir "optimamente" un canal para salida (envio) de datos
        bw.write(message);
        bw.flush();
        socket.shutdownOutput(); // esta mierda de linea sirve para avisar que YA no va a enviar mas datos, asi los puede levantar desde el metodo processConnection de la instancia de la clase SimpleServer
    }
    
    public void recieve() throws IOException{ 
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is)); // operaciones par abrir un canal de entrada (recepcion) de datos
        log("[Client] - Response from server:");
		String line = br.readLine();
		while (line != null) {
			log("[Client] recieved : " + line);
			line = br.readLine();
		}
    }
    
    public void close () throws IOException {
    	if (this.socket != null) {
    		this.socket.close();
    	}
    }

	private void log (String s) {
		System.out.println(s);
	}
}