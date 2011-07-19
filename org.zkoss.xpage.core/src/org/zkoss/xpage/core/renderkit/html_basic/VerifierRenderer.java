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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.zkoss.xpage.core.component.Action;
import org.zkoss.xpage.core.component.Verifier;
import org.zkoss.xpage.core.component.ZKComponentBase;
import org.zkoss.xpage.core.util.Log;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.embed.Renders;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;

import com.ibm.domino.xsp.module.nsf.NotesContext;

public class VerifierRenderer extends ZKRendererBase {

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
	protected Component createZKComponent(Page page,ZKComponentBase zcomp){
		Button button = new Button();
		button.setId(zcomp.getId());
		button.setLabel("Hi "+getUserName()+", Click me to verify zk ajax");
		button.addEventListener("onClick", new EventListener(){
			public void onEvent(Event evt)
					throws Exception {
				Messagebox.show("Hi "+getUserName()+", zk is ajax update is running");
			}});
		return button;
	}
}
