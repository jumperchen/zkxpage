/*
 * Copyright 2011 Potix Corporation. All Rights Reserved.
 * 
 * Licensed under the GNU GENERAL PUBLIC LICENSE Version 3 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.gnu.org/licenses/gpl-3.0.txt
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.zkoss.xpage.core.util;

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
	
	@SuppressWarnings("unchecked")
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
