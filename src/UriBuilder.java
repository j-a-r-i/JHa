
public class UriBuilder {
	private StringBuilder sb;
	private boolean firstParam;

	public UriBuilder() {
		sb = new StringBuilder();
		firstParam = true;
	}
	
	public UriBuilder setHost(String host) {
		sb.append(host);
		return this;
	}
	
	public UriBuilder addParam(String name, String value) {
		if (firstParam)
			sb.append('?');
		else
			sb.append('&');
		
		firstParam = false;
		sb.append(name);
		sb.append('=');
		sb.append(value);
		return this;
	}
	
	public String toString() {
		return sb.toString();
	}
}
