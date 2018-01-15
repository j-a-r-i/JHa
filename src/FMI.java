import java.io.InputStream;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

class FmiData {
	private long  epoch;
	private float temperature;
	private float devPoint;
	private float windSpeed;
	private float precipitation;
}

public class FMI extends Downloader {

	private static final String SITE = "http://data.fmi.fi/fmi-apikey/";
	private static final String TAG_POSITION = "positions";
	private static final String TAG_RESULT = "doubleOrNilReasonTupleList";	
	private static final List<String> FLD_NAMES = Arrays.asList(
			"temperature",
			"dewpoint",
			"windspeedms",
			"precipitation1h");
	
	public FMI() {
	}
	
	@Override
	protected void makeUrl(UriBuilder uri) {
    	uri.setHost(SITE + Config.getFmiKey() + "/wfs");
    	uri.addParam("request",        "getFeature");
    	uri.addParam("storedquery_id", "fmi::forecast::hirlam::surface::point::multipointcoverage");
    	uri.addParam("place",          "oittaa");
    	uri.addParam("parameters",     String.join(",", FLD_NAMES));	
	}

	@Override
	protected void fillRequest(HttpURLConnection con) {	
		con.setRequestProperty("Content-Type", "application/json");	
	}

	private void parsePosition(String line) {
		System.out.println("P[" + line + "]");
	}

	private void parseResult(String line) {
		System.out.println("R[" + line + "]");
	}

	public void parse(InputStream stream) {
    	XMLInputFactory factory = XMLInputFactory.newInstance();
    	Consumer<String> parseFn = null;
    	try {
			XMLEventReader reader = factory.createXMLEventReader(stream);
			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				if (event.isStartElement()) {
					String tag = event.asStartElement().getName().getLocalPart();
					
					switch (tag) {
					case TAG_POSITION:
						parseFn = this::parsePosition;
						break;
					case TAG_RESULT:
						parseFn = this::parseResult;
						break;
					default:
						parseFn = null;
						break;
					}
				}
				else if (event.isEndElement()) {
					parseFn = null;
				}
				else if (event.isCharacters()) {
					String text = event.asCharacters().getData().trim();
					
					if ((text.length() > 0) && (parseFn != null)) {
						String[] data = text.split("\n");
						for (String line : data)
							parseFn.accept(line.trim());
						System.out.println("..");
					}
				}
			}
    	} catch (XMLStreamException e) {
    		e.printStackTrace();
    	}
	}
}
