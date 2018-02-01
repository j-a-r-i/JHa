import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Adsl {
	private Socket socket;
	private BufferedReader r;
	private PrintWriter w;

	public Adsl() {
 		try {
			socket = new Socket(Config.getAdslHost(), 23);
	    	socket.setKeepAlive(true);
	    	r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    	w = new PrintWriter(socket.getOutputStream(), true);
	    	
	    	readUntil("ogin: ");
	    	w.println(Config.getAdslUser());
	    	
	    	readUntil("assword: ");
	    	w.println(Config.getAdslPasswd());

	    	readUntil(" > ");
	    	w.println("help");
	    	
	    	readUntil(" > ");
	    	w.println("quit");
	    	socket.close();    	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void isAdslOn() {
		InetAddress inet = Config.getAdslHost();
		
		try {
			System.out.println("ping " + (inet.isReachable(3000) ? "OK" : "FAILED"));
		} catch (IOException e) {
			System.out.println("ping ERROR");
			e.printStackTrace();
		} 
	}
    
	private void readUntil(String match) throws IOException {
    	StringBuilder buf = new StringBuilder();
    	int ch = 0;
    	while ((ch = r.read()) != -1) {
    		buf.append((char)ch);
    		if (buf.toString().endsWith(match)) 
    			break;
    	    System.out.print((char)ch);
    	}	    	
		
	}
}
