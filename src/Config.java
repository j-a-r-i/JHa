import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;

/** Store for configuration data.
 */
public class Config {
    private static String stravaToken;
    private static String fmiKey;
    private static String redisServer;
    
    /** Load configuration data from properties file.
     *  This method must be called before accessing any
     *  configuration data.
     */
    public static void load() {
		Properties prop = new Properties();
	
		try (InputStream is = new FileInputStream("/home/config.properties")) {
		    prop.load(is);
	
		    stravaToken = prop.getProperty("strava.token");
		    fmiKey = prop.getProperty("fmi.key");
		    redisServer = prop.getProperty("redis.server");
		}
		catch (Exception e) {
		    Log.error("load configuration", e);
		}
    }

    /** Port for the HTTP server.
    */
    public static int getServerPort() {
    	return 8001;
    }
    
    /** Get strava API token
     */
    public static String getStravaToken() {
    	return stravaToken;
    }
    
    /** Get FMI API key
     */
    public static String getFmiKey() {
    	return fmiKey;
    }
    
    /** Get FMI API key
     */
    public static String getRedisServer() {
    	return redisServer;
    }
}
