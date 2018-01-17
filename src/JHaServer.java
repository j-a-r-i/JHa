import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;
import java.util.logging.Logger;
//import static Tag;
import java.util.stream.Collectors;
import java.net.URI;

//------------------------------------------------------------------------------
abstract class BaseHandler implements HttpHandler {
    private final static Logger LOGGER = Logger.getLogger(BaseHandler.class.getName());
    private String path;
	
    public BaseHandler(String path) {
	this.path = path;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
	URI uri = t.getRequestURI();
	
	LOGGER.info("MyHandler:"+path + " path:" + uri.getPath());

	String response = content();
	Headers h = t.getResponseHeaders();
	h.set("Content-Type","text/html");
	
	t.sendResponseHeaders(200, response.length());
	OutputStream os = t.getResponseBody();
	os.write(response.getBytes());
	os.close();
    }

    abstract String content();
    
    public String getPath() {
	return path;
    }
}

//------------------------------------------------------------------------------
class RedisHandler extends BaseHandler {
    private final static Logger LOGGER = Logger.getLogger(RedisHandler.class.getName());
    private MyRedis redis;
    private String key;

    public RedisHandler(String rkey, MyRedis r) {
	super("/data/"+rkey);
	redis = r;
	key = rkey;
    }

    private String row(String line) {
	//LOGGER.info(line);
	String[] values = line.split(",");
	return Tag.tag("tr",
		       Tag.tag("td", values[0]),
		       Tag.tag("td", values[1]));
    }

    @Override
    public String content() {
	String data = Tag.tag("div",
			      Tag.tag("h1", "Redis " + key),
			      Tag.tag("table",
				      redis.values(key).stream()
				      .map(this::row)
				      .collect(Collectors.joining(""))
				      ));
    	return data;
    }
}

//------------------------------------------------------------------------------
class MyHandler1 extends BaseHandler {
    public MyHandler1() {
	super("/one");
    }
	
    @Override
    public String content() {
    	return "This is one.";
    }
}

//------------------------------------------------------------------------------
class LinkHandler extends BaseHandler {
    private String linkStr;

    public LinkHandler(MyRedis redis) {
	super("/links");

	linkStr = redis.keys().stream()
	    .map(i -> Tag.tag("li",
			      Tag.a("/data/"+i, i)))
	    .collect(Collectors.joining(""));

    }
	
    @Override
    public String content() {
	String links = Tag.tag("div",
			       Tag.tag("h1", "Data sources"),
			       Tag.tag("ul",
				       linkStr));
	 
	return links;
    }
}

//------------------------------------------------------------------------------
public class JHaServer {
    private static Logger LOGGER = null;

    static {
	System.setProperty("java.util.logging.SimpleFormatter.format",
			   "[%1$tF %1$tT] [%4$-7s] %5$s %n");
	LOGGER = Logger.getLogger(JHaServer.class.getName());
    }
    
    public static void main(String[] args) {
	LOGGER.info("started");
        Config.load();
	MyRedis redis = new MyRedis();

	BaseHandler[] handlers = {
	    new MyHandler1(),
	    new LinkHandler(redis)
	};

        HttpServer server;
	try {
	    server = HttpServer.create(new InetSocketAddress(Config.getServerPort()), 0);
	    for (BaseHandler h : handlers) {
		server.createContext(h.getPath(), h);
	    }
	    for (String key : redis.keys()) {
		RedisHandler h = new RedisHandler(key, redis);
		server.createContext(h.getPath(), h);
	    }
	    server.setExecutor(null); // creates a default executor
	    server.start();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	//LOGGER.info("close redis");
	//redis.close();
    }
}
