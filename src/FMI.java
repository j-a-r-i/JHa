import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class FMI extends Downloader {

	private static final String SITE = "http://data.fmi.fi/fmi-apikey/";
	private static final String TAG_POSITION = "positions";
	private static final String TAG_RESULT = "doubleOrNilReasonTupleList";	
	private static final List<String> VALID_TAGS = Arrays.asList(
			TAG_POSITION,
			TAG_RESULT
	);
	
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

	public void parse(InputStream stream) {
    	XMLInputFactory factory = XMLInputFactory.newInstance();
    	boolean enabled = false;
    	try {
			XMLEventReader reader = factory.createXMLEventReader(stream);
			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				if (event.isStartElement()) {
					String tag = event.asStartElement().getName().getLocalPart();

					if (VALID_TAGS.contains(tag)) {
						System.out.println(">>"+tag);
						enabled = true;
					}					
				}
				else if (event.isEndElement()) {
					String tag = event.asEndElement().getName().getLocalPart();
					
					if (VALID_TAGS.contains(tag)) {
						System.out.println("<<"+tag);
						enabled = false;
					}
				}
				else if (event.isCharacters()) {
					String text = event.asCharacters().getData().trim();
					
					if ((text.length() > 0) && enabled) {
						String[] data = text.split("\n");
						for (String line : data)
							System.out.println("["+line.trim()+"]");
					}
				}
			}
    	} catch (XMLStreamException e) {
    		e.printStackTrace();
    	}
	}
}
