import java.net.HttpURLConnection;
import java.lang.StringBuilder;

public class Strava extends Downloader {
    private String SITE = "https://www.strava.com/api/v3/athlete/activities";
    
    public Strava() {
    }

    @Override
    protected void makeUrl(UriBuilder uri) {    	
    	uri.setHost(SITE);
    	uri.addParam("page",         "1");
    	uri.addParam("access_token", Config.getStravaToken());
    }

	@Override
	protected void fillRequest(HttpURLConnection con) {
		con.setRequestProperty("Content-Type", "application/json");	
	}			   
}
