import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

class ConfigSite {
    private String name;
    private String host;
    private String user;
    private String passwd;

    public ConfigSite(String n) {
    	name = n;
    }

    public void load(Properties prop) {
		host = prop.getProperty(name + ".host");
		user = prop.getProperty(name + ".user");
		passwd = prop.getProperty(name + ".passwd");
    }

    public InetAddress getHost() {
		try {
		    return InetAddress.getByName(host);
		} catch (UnknownHostException e) {
		    e.printStackTrace();
		}
		return null;
    }
	
    public String getUser() {
    	return user;
    }
	
    public String getPasswd() {
    	return passwd;
    }
}

/** Store for configuration data.
 */
public class Config {
    private static String stravaToken;
    private static String fmiKey;
    private static String redisServer;
    private static ConfigSite adsl = new ConfigSite("adsl");
    private static ConfigSite smtp = new ConfigSite("smtp");
    private static String emailTo;
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
		    adsl.load(prop);
		    smtp.load(prop);
		    emailTo = prop.getProperty("email.to");
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
    
    /** Get redis server
     */
    public static String getRedisServer() {
    	return redisServer;
    }

    /** Get ADSL host.
     */
    public static ConfigSite getAdsl() {
    	return adsl;
    }

    /** Get SMTP host.
     */
    public static ConfigSite getSmtp() {
    	return smtp;
    }

    /** Get email recipient
     */
    public static String getEmailTo() {
    	return emailTo;
    }
}
