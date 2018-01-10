import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

abstract class BaseHandler implements HttpHandler {
	private String path;
	
	public BaseHandler(String path) {
		this.path = path;
	}

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

	public String getPath() {
		return path;
	}
}

class MyHandler1 extends BaseHandler {
	public MyHandler1() {
		super("/one");
	}
	
    @Override
    public void handle(HttpExchange t) throws IOException {
    	write(t, "This is one.");
    }
}

class MyHandler2 extends BaseHandler {
	public MyHandler2() {
		super("/two");
	}
	
    @Override
    public void handle(HttpExchange t) throws IOException {
    	write(t, "This is two.");
    }
}

public class JHaServer {
	public static void main(String[] args) {
		BaseHandler[] handlers = {
				new MyHandler1(),
				new MyHandler2()
		};
		
        HttpServer server;
        Config.load();
		try {
			server = HttpServer.create(new InetSocketAddress(Config.getServerPort()), 0);
			for (BaseHandler h : handlers) {
				server.createContext(h.getPath(), h);
			}
	        server.setExecutor(null); // creates a default executor
	        server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
