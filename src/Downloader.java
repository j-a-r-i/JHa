import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Downloader {

	abstract protected void makeUrl(UriBuilder uri);

	abstract protected void fillRequest(HttpURLConnection con);
	
	static public void dumpStream(InputStream inStream) {		
		try {
			String inputLine;
		    BufferedReader in = new BufferedReader(
					   new InputStreamReader(
								 inStream));
			while ((inputLine = in.readLine()) != null) 
				System.out.println(inputLine);
			in.close();		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public void download(Consumer<InputStream> parseFn) {
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
	
		    parseFn.accept(con.getInputStream());
		}
		catch(IOException e) {
		}
	}
}
