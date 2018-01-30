import org.apache.commons.net.telnet.TelnetClient;

public class Adsl {
	private TelnetClient client;
	
	public Adsl() {
		client = new TelnetClient();
		try {
			client.connect(Config.getAdslHost(), 23);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
