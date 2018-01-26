import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class StravaActivity {
	private float distance;
	private long moving_time;
	private long elapsed_time;
	private String type;
	private Date start_date;
	
	public StravaActivity() {
	}
	
	@Override
	public String toString() {
		return "act [type=" + type + ", distance=" + distance + ", moving=" + moving_time + ", elapsed=" + elapsed_time  + ", start=" + start_date+ "]";
	}
}

public class Strava extends Downloader {
    private static final String SITE = "https://www.strava.com/api/v3/athlete/activities";
    
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

	public void parse(InputStream stream) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		
		StravaActivity[] acts = gson.fromJson(new InputStreamReader(stream), StravaActivity[].class);
		for (StravaActivity a : acts) {
			System.out.println(a);
		}
	}
}
