package view.help;

import java.util.WeakHashMap;

public class HelpPageRegistry {

	/** The mapping of objects to URLs. */
	private static final WeakHashMap HELP_MAP = new WeakHashMap();
	
	/**
	 * Returns the URL for a particular object as set via <CODE>setURL</CODE>.
	 * If there is a direct mapping from the object to a URL, that URL is
	 * returned. If there is no direct mapping, a mapping from this object's
	 * class to a URL is attempted. If <i>that</i> yields nothing, then a
	 * mapping from the object's superclass to a URL is attempted. If no
	 * superclass yields a URL, then <CODE>null</CODE> is returned.
	 * 
	 * @param object
	 *            the object to get help for
	 * @return a URL of help for this object, or <CODE>null</CODE> if no help
	 *         for this object exists
	 * @see #setURL(Object, String)
	 */
	public static String getPageURL(Object object) {
		String url = (String) HELP_MAP.get(object);
		if (url != null)
			return url;
		Class c = object instanceof Class ? (Class) object : object.getClass();
		while (c != null) {
			url = (String) HELP_MAP.get(c);
			if (url != null)
				return url;
			url = "/DOCS/" + c.getName() + ".html";
			if (c.getResource(url) != null)
				return url;
			c = c.getSuperclass();
		}
		return DEFAULT_HELP;
	}
	
	/**
	 * Associates a particular object with a URL. This object may be a
	 * particular instance (in which case when this instance gets its help it
	 * gets it immediately) or a Class object holding the class or superclass of
	 * the instance.
	 * 
	 * @param object
	 *            the key which will map to a URL
	 * @param url
	 *            the string representation of the URL to visit
	 * @see #getURL(Object)
	 */
	public static void setURL(Object object, String url) {
		HELP_MAP.put(object, url);
	}
	

	/** The default URL in case there is no help for a subject. */
	public static final String DEFAULT_HELP = "/DOCS/nohelp.html";

}
