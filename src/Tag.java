
public class Tag {
    public static String tag(String tag, String...childs)  {
	StringBuffer buf = new StringBuffer();
	int tagEnd = tag.indexOf(" ");
		
	buf.append('<');
	buf.append(tag);
	buf.append('>');
	for (String c : childs) {
	    buf.append(c);
	}
	buf.append('<');
	buf.append('/');
	if (tagEnd == -1)
	    buf.append(tag);
	else
	    buf.append(tag.substring(0,tagEnd));  // strip attributes
	buf.append('>');
		
	return buf.toString();
    }

    public static String a(String href, String data) {
	return Tag.tag("a href=\"" + href + "\"", data);
    }
}
