import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	public static Logger LOGGER = null;
	public static void init() {
		LOGGER = Logger.getLogger("JHA");
	}
	
	public static void error(String msg, Exception ex) {
		LOGGER.log(Level.SEVERE, msg, ex);
	}

	public static void info(String msg) {
		LOGGER.info(msg);
	}
}
