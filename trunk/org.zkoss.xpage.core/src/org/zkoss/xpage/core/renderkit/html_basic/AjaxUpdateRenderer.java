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
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.zkoss.xpage.core.bean.AuContext;
import org.zkoss.xpage.core.bean.JsfContext;
import org.zkoss.xpage.core.component.AjaxUpdate;

public class AjaxUpdateRenderer extends javax.faces.render.Renderer {
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
	throws IOException {
		if(!(component instanceof AjaxUpdate)){
			throw new IllegalArgumentException("unsupported component "+component);
		}
		final AjaxUpdate zcomp = (AjaxUpdate)component;
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", component);
		writer.writeAttribute("id", component.getClientId(context), null);
		JsfContext jsfc = JsfContext.instance();
		if(jsfc.isAjaxPartialRefresh()){
			writer.startElement("script",null);
			writer.writeAttribute("type", "text/javascript",  null);
			AuContext au = AuContext.instance();
			Iterator iter = au.iteratorScripts();
			while(iter.hasNext()){
				String script = (String)iter.next();
				writer.write(script);
			}
			au.clearScripts();
			writer.endElement("script");
		}
		writer.endElement("div");
	}
}
