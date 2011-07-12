package org.zkoss.xpage.core.web;

/** a simple helper to do log**/
public class Log {

	private static final org.zkoss.util.logging.Log log = org.zkoss.util.logging.Log
			.lookup(Log.class);

	public static void log(Object base, String message) {
		log.error(toBasePrefix(base)+(message==null?"":message));
	}
	
	private static String toBasePrefix(Object base) {
		if(base==null) return "";
		StringBuilder sb = new StringBuilder();
		if(base instanceof Class) {
			sb.append("[").append(((Class) base).getSimpleName().toString()).append("]");
		}else {
			sb.append("[").append(base.getClass().getSimpleName().toString()).append("]");
		}
		return sb.toString();
	}

	public static void error(Object base, String message, Throwable t) {
		log.error(toBasePrefix(base)+(message==null?t.getMessage():message), t);
	}

}
