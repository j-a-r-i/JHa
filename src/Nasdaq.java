import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.net.HttpURLConnection;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.StringBuilder;

class NasdaqData {
	private String company;
	private float value;

	public NasdaqData() {
		company = null;
		value = 0.0f;
	}

	public void setValue(float val) {
		value = val;
	}

	public float getValue() {
		return value;
	}

	public void setCompany(String val) {
		company = val;
	}

	public String getCompany() {
		return company;
	}
}

public class Nasdaq extends Downloader {
    public static final int NOKIA = 24311;
    public static final int CITYCON = 24249;
    
    private static final String SITE = "https://www.nasdaqomxnordic.com/webproxy/DataFeedProxy.aspx";
    private int instrument;
    private boolean history;
    private NasdaqData output;
    
    public Nasdaq() {
    	history = false;
    	output = new NasdaqData();
    }

    public void setInstrument(int hexCode) {
    	instrument = hexCode;
    }
    
    public void setHistory(boolean val) {
    	history = val;
    }
    
    public NasdaqData getResult() {
    	return output;
    }
    
    @Override
    protected void makeUrl(UriBuilder uri) {
		uri.setHost(SITE);
		if (history) {
			uri.addParam("Subsystem", "History");
			uri.addParam("Action",    "GetDataSeries");
		}
		else {
			uri.addParam("Subsystem", "Prices");
			uri.addParam("Action",    "GetInstrument");
		}
		uri.addParam("Instrument", "HEX"+Integer.toString(instrument));
		if (history) {
			uri.addParam("FromDate", "2017-12-01");
		}		
    }

	@Override
	protected void fillRequest(HttpURLConnection con) {
	    //con.setRequestProperty("Content-Type", "application/json");
	    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
	}			   
   
    /** Parse XML output from the API.
     *  This is public for unit test.
     *  TODO refactor this method!
     */
    public void parsePrice(InputStream stream) {
    	XMLInputFactory factory = XMLInputFactory.newInstance();
    	try {
			XMLEventReader reader = factory.createXMLEventReader(stream);
			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				if (event.isStartElement()) {
					StartElement element = event.asStartElement();

					System.out.println(element.getName().getLocalPart());
					if (element.getName().getLocalPart().equals("inst")) {
						Attribute attr = element.getAttributeByName(new QName("nm"));
						if (attr != null) {
							output.setCompany(attr.getValue());
						}
						attr = element.getAttributeByName(new QName("cp"));
						if (attr != null) {
							output.setValue(Float.parseFloat(attr.getValue()));
						}
					}
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
    }
}
