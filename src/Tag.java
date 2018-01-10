
public class Tag {
	public static String T(String tag, String...childs)  {
		StringBuffer buf = new StringBuffer();
		
		buf.append('<');
		buf.append(tag);
		buf.append('>');
		for (String c : childs) {
			buf.append(c);
		}
		buf.append('<');
		buf.append('/');
		buf.append(tag);
		buf.append('>');
		
		return buf.toString();
	}
}
