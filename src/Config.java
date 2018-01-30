import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

/** Store for configuration data.
 */
public class Config {
    private static String stravaToken;
    private static String fmiKey;
    private static String redisServer;
    private static String adslHost;
    private static String adslUser;
    private static String adslPasswd;
    
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
		    adslHost = prop.getProperty("adsl.host");
		    adslUser = prop.getProperty("adsl.user");
		    adslPasswd = prop.getProperty("adsl.passwd");
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

    /** Get ADSL server hostname.
     */
	public static InetAddress getAdslHost() {
		try {
			return InetAddress.getByName(adslHost);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getAdslUser() {
		return adslUser;
	}
	
	public static String getAdslPasswd() {
		return adslPasswd;
	}
}
