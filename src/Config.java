import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;

/** Store for configuration data.
 */
public class Config {
    private static String stravaToken;
    private static String fmiKey;

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
		}
		catch (Exception e) {
		    System.out.println("Error loading configuration!");
		}
    }

    /** Port for the HTTP server.
    */
    public static int getServerPort() {
    	return 8000;
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
}
