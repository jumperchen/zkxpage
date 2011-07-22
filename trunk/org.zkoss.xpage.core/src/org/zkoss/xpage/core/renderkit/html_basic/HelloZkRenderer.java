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
package org.zkoss.xpage.core.renderkit.html_basic;

import java.lang.reflect.Method;

import org.zkoss.xpage.core.component.ZulComponentBase;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;

import com.ibm.domino.xsp.module.nsf.NotesContext;
/**
 * this is a very simple component's renderer, it test zk ajax and access notes context in zk au 
 * @author Dennis Chen
 *
 */
public class HelloZkRenderer extends ZulRendererBase {

	private static String getUserName(){
		try{
			NotesContext nc = NotesContext.getCurrentUnchecked();
			if(nc==null) return "no context";
			Object sess = nc.getCurrentSession();
			if(sess==null) return "no session";
			
			Method sm = sess.getClass().getMethod("getCommonUserName");
            String username = (String)sm.invoke(sess, (Object[])null);
            return username;
		}catch(Throwable x){
			x.printStackTrace();
			return x.getMessage();
		}
	}
	
	@Override
	protected Component createZKComponent(Page page,ZulComponentBase zcomp){
		Button button = new Button();
		button.setLabel("Hi "+getUserName()+", Click me to verify zk ajax");
		button.addEventListener("onClick", new EventListener(){
			public void onEvent(Event evt)
					throws Exception {
				Messagebox.show("Hi "+getUserName()+", zk is ajax update is running");
			}});
		return button;
	}
}
