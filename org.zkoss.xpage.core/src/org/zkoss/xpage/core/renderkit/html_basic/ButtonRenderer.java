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
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.embed.Renders;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;

public class ButtonRenderer extends javax.faces.render.Renderer {
	
	private static final Log log = Log.lookup(ButtonRenderer.class);
	
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
		writer.writeText("You should see a ZK button in a green block, when clicking on it, it show a zk message box", null);
		
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
							button.setLabel("CLICK ME");
							button.addEventListener("onClick", new EventListener(){
								public void onEvent(Event evt)
										throws Exception {
									Messagebox.show("hi user");
								}});
							log.warning("current session is "+request.getSession().getId()+",desktop is ::"+Executions.getCurrent().getDesktop());
						}
					}, null, writer);
		} catch (ServletException e) {
			log.error(e.getMessage());
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
