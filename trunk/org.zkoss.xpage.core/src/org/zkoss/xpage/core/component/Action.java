/* 
{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Jul 22, 2011 , Created by Dennis Chen
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.xpage.core.component;
/**
 * a action that will be invoke in {@link ZulComponentBase#execute(Action)}
 * @author Dennis Chen
 *
 */
public interface Action {

	public void doAction(ZulComponentBase zcomp);
}
