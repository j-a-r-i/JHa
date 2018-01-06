import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

abstract class BaseHandler implements HttpHandler {
    public void write(HttpExchange t, String response) {
	try {
	    t.sendResponseHeaders(200, response.length());
	    OutputStream os = t.getResponseBody();
	    os.write(response.getBytes());
	    os.close();
	}
	catch (IOException e) {
	}
    }
}

public class JHaServer {
	
    static class MyHandler1 extends BaseHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
        	write(t, "This is one.");
        }
    }

    static class MyHandler2 extends BaseHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
        	write(t, "This is two.");
        }
    }

	public static void main(String[] args) {
        HttpServer server;
        Config.load();
		try {
			server = HttpServer.create(new InetSocketAddress(8000), 0);
	        server.createContext("/one", new MyHandler1());
	        server.createContext("/two", new MyHandler2());
	        server.setExecutor(null); // creates a default executor
	        server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
