import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;

/** Store for configuration data.
 */
public class Config {
    private static String stravaToken;

    /** Load configuration data from properties file.
     *  This method must be called before accessing any
     *  configuration data.
     */
    public static void load() {
		Properties prop = new Properties();
	
		try (InputStream is = new FileInputStream("/home/config.properties")) {
		    prop.load(is);
	
		    stravaToken = prop.getProperty("strava.token");
		}
		catch (Exception e) {
		    System.out.println("Error loading configuration!");
		}
    }

    /** Get strava API token
     */
    public static String getStravaToken() {
    	return stravaToken;
    }
}
