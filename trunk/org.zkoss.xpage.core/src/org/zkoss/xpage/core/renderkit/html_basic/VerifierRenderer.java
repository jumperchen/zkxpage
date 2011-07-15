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

import org.zkoss.xpage.core.util.Log;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.embed.Renders;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;

import com.ibm.domino.xsp.module.nsf.NotesContext;

public class VerifierRenderer extends javax.faces.render.Renderer {

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
	public void encodeBegin(FacesContext context, UIComponent component)
	throws IOException {
		
		final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		final HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		
		// Note, the component passed in is the ExampleControl
		// ExampleControl control = (ExampleControl) component;
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", component);
		writer.writeAttribute("style", "border:orange solid thin;width:400px;", null);
		writer.writeText("Hi "+getUserName()+". You should able to see a ZK button in a green block.", null);
		writer.write("<br/>");
		writer.writeText("Please click on the button, it should shows a zk message box with your name", null);
		writer.write("<br/>");
		
		HttpSession sess = request.getSession();
		
		String session_attr1 = (String)sess.getAttribute("session_attr1");
		writer.writeText(", session_attr1="+session_attr1, null);
		request.getSession().setAttribute("session_attr1", "set by "+this+",date:"+new Date());
		writer.startElement("div", null);
		writer.writeAttribute("style", "border:green solid thin;", null);
		// ZK component
		ServletContext svlctx = (ServletContext)context.getExternalContext().getContext();
		try {
			Renders.render(svlctx, request, response, 
					new GenericRichlet() {
						public void service(Page page) throws Exception {
							
							Button button = new Button();
							button.setPage(page);
							button.setLabel("Click me to verify zk ajax");
							button.addEventListener("onClick", new EventListener(){
								public void onEvent(Event evt)
										throws Exception {
									Messagebox.show("Hi "+getUserName()+", zk is ajax update is running");
								}});
							Log.log(this,"current session is "+request.getSession().getId()+",desktop is ::"+Executions.getCurrent().getDesktop());
						}
					}, null, writer);
		} catch (ServletException e) {
			Log.error(this,e.getMessage(),e);
			throw new IOException(e.getMessage());
		} 
		writer.endElement("div");
	}
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}
}
