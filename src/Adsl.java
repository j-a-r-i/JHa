import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Adsl {
	private Socket socket;
	private BufferedReader r;
	private PrintWriter w;

	public Adsl() {
 		try {
			socket = new Socket("192.168.0.254", 23);
	    	socket.setKeepAlive(true);
	    	r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    	w = new PrintWriter(socket.getOutputStream(), true);
	    	
	    	readUntil("ogin: ");
	    	w.println("admin");
	    	
	    	readUntil("assword: ");
	    	w.println("1234");

	    	socket.close();    	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
