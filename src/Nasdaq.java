import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
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
    
    /** Parse XML output from the API.
     *  This is public for unit test.
     *  TODO refactor this method!
     */
    public NasdaqData parseXml(String str) {
    	NasdaqData data = new NasdaqData();
    	XMLInputFactory factory = XMLInputFactory.newInstance();
    	ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
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
							data.setCompany(attr.getValue());
						}
						attr = element.getAttributeByName(new QName("cp"));
						if (attr != null) {
							data.setValue(Float.parseFloat(attr.getValue()));
						}
					}
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
    	return data;
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
