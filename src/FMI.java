import java.net.HttpURLConnection;

public class FMI extends Downloader {

	private static final String SITE = "http://data.fmi.fi/fmi-apikey/"; 
	
	public FMI() {
	}
	
	@Override
	protected void makeUrl(UriBuilder uri) {
    	uri.setHost(SITE + Config.getFmiKey() + "/wfs");
    	uri.addParam("request",        "getFeature");
    	uri.addParam("storedquery_id", "fmi::forecast::hirlam::surface::point::multipointcoverage");
    	uri.addParam("place",          "oittaa");
    	uri.addParam("parameters",     "temperature,dewpoint,windspeedms,precipitation1h");
    	
	}

	@Override
	protected void fillRequest(HttpURLConnection con) {	
		con.setRequestProperty("Content-Type", "application/json");	
	}

}
