import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

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
    
    public void parseXml(InputStream stream, String tag, Consumer<StartElement> fnHandle) {
    	XMLInputFactory factory = XMLInputFactory.newInstance();
    	try {
			XMLEventReader reader = factory.createXMLEventReader(stream);
			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				if (event.isStartElement()) {
					StartElement element = event.asStartElement();

					System.out.println(element.getName().getLocalPart());
					if (element.getName().getLocalPart().equals(tag)) {
						fnHandle.accept(element);
					}
				}
			}
    	} catch (XMLStreamException e) {
    		e.printStackTrace();
    	}

    }
}
