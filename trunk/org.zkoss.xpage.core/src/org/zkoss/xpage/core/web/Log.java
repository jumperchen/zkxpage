/* 
{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		July 13, 2011 , Created by dennischen
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.xpage.core.web;

/** a simple helper to do log**/
public class Log {

	private static boolean DEVELOPING = false;
	
	
	private static final org.zkoss.util.logging.Log log = org.zkoss.util.logging.Log
			.lookup(Log.class);

	public static void log(Object base, String message) {
		message = toBasePrefix(base)+(message==null?"":message);
		if(DEVELOPING){
			log.error(message);
		}else{
			log.debug(message);
		}
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
