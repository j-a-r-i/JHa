import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuilder;

public class Strava {
    private String SITE = "https://www.strava.com/api/v3/athlete/activities?";
    
    public Strava() {
    }

    private String MakeUrl() {
		StringBuilder sb = new StringBuilder();
	
		sb.append(SITE);
		sb.append("page=1");
		sb.append('&');
		sb.append("access_token=");
		sb.append(Config.getStravaToken());
	
		return sb.toString();
    }
    
    public void download() {
		System.out.println("download Strava");
		try {
		    URL url = new URL(MakeUrl());	
		    HttpURLConnection con = (HttpURLConnection) url.openConnection();
	
		    con.setRequestMethod("GET");
	
		    con.setRequestProperty("Content-Type", "application/json");
	
		    con.setUseCaches(false);
		    con.setDoOutput(true);
	
		    BufferedReader in = new BufferedReader(
							   new InputStreamReader(
										 con.getInputStream()));
		    String inputLine;
	
		    while ((inputLine = in.readLine()) != null) 
			System.out.println(inputLine);
		    in.close();
		}
		catch(IOException e) {
		}
	}			   
}
