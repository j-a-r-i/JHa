import java.net.HttpURLConnection;
import java.lang.StringBuilder;

public class Strava extends Downloader {
    private String SITE = "https://www.strava.com/api/v3/athlete/activities?";
    
    public Strava() {
    }

    @Override
    protected String makeUrl() {
		StringBuilder sb = new StringBuilder();
	
		sb.append(SITE);
		sb.append("page=1");
		sb.append('&');
		sb.append("access_token=");
		sb.append(Config.getStravaToken());
	
		return sb.toString();
    }

	@Override
	protected void fillRequest(HttpURLConnection con) {
		con.setRequestProperty("Content-Type", "application/json");	
	}			   
}
