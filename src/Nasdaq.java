import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuilder;

public class Nasdaq {
    public static final int NOKIA = 24311;
    public static final int CITYCON = 24249;
    
    private static final String SITE = "https://www.nasdaqomxnordic.com/webproxy/DataFeedProxy.aspx?";
    
    public Nasdaq() {
    }

    private String makeUrl(int hexCode) {
		StringBuilder sb = new StringBuilder();
		boolean history = false;
	
		sb.append(SITE);
		if (history)
		    sb.append("Subsystem=History");
		else
		    sb.append("Subsystem=Prices");
		sb.append('&');
		if (history)
		    sb.append("Action=GetDataSeries");
		else
		    sb.append("Action=GetInstrument");
		sb.append('&');
		sb.append("Instrument=HEX");
		sb.append(hexCode);
		if (history) {
		    sb.append('&');
		    sb.append("FromDate=2017-12-01");
		}		
		return sb.toString();
    }
    
    public void download(int hexCode) {
		System.out.println("download Nasdaq " + hexCode);
		try {
		    URL url = new URL(makeUrl(hexCode));	
		    HttpURLConnection con = (HttpURLConnection) url.openConnection();
	
		    con.setRequestMethod("GET");
	
		    //con.setRequestProperty("Content-Type", "application/json");
		    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
	
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
		    System.err.println("IOException: " + e.getMessage());
		}
    }			   
}
