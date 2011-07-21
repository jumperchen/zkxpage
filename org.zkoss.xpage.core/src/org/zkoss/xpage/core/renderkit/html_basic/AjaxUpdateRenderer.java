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

//import org.zkoss.xpage.core.bean.AuContext;
import org.zkoss.xpage.core.bean.JsfContext;
import org.zkoss.xpage.core.component.AjaxUpdate;

import com.ibm.xsp.ajax.AjaxUtil;
import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.resource.ScriptResource;


public class AjaxUpdateRenderer extends javax.faces.render.Renderer{
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

//		if(au.hasScript() && jsfc.isAjaxPartialRefresh() && jsfc.isAjaxRendered(component)){
//			writer.write("<!-- XSP_UPDATE_HEADER_START -->\n");//special tag for script or header element
//			writer.startElement("script",null);
//			writer.writeAttribute("type", "text/javascript",  null);
//			Iterator iter = au.iteratorScripts();
//			while(iter.hasNext()){
//				String script = (String)iter.next();
//				writer.write(script);
//			}
//			au.clearScripts();
//			writer.endElement("script");
//			writer.write("<!-- XSP_UPDATE_HEADER_END -->\n");//special tag for script or header element
			
			
//			FacesContext ctx = FacesContext.getCurrentInstance();
//			boolean isRendering = AjaxUtil.isRendering(FacesContext.getCurrentInstance());
//            AjaxUtil.setRendering(ctx, true);
//            try {
//            	UIViewRootEx vrex = ((UIViewRootEx)ctx.getViewRoot());
//    			Iterator iter = au.iteratorScripts();
//    			while(iter.hasNext()){
//    				String script = (String)iter.next();
//    				ScriptResource r = new ScriptResource();
//                    r.setClientSide(true);
//                    r.setContents(script);
//                    vrex.addEncodeResource(r);
//    			}
//            } finally {
//                AjaxUtil.setRendering(ctx, isRendering);
//            }
			
//		}
		writer.endElement("div");
	}
}
