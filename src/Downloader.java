import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class Downloader {

	abstract protected void makeUrl(UriBuilder uri);

	abstract protected void fillRequest(HttpURLConnection con);
	   
    public void download() {
		System.out.println("download...");
		try {
			UriBuilder uri = new UriBuilder();
			makeUrl(uri);
		    URL url = new URL(uri.toString());	
		    HttpURLConnection con = (HttpURLConnection) url.openConnection();
	
		    con.setRequestMethod("GET");
	
		    fillRequest(con);
	
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
