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
package org.zkoss.xpage.core.renderkit.html_basic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.lang.Classes;
import org.zkoss.xpage.core.bean.JsfContext;
import org.zkoss.xpage.core.component.ZulComponentBase;
import org.zkoss.xpage.core.util.Log;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zkplus.embed.Bridge;
import org.zkoss.zkplus.embed.Renders;
import org.zkoss.zul.impl.XulElement;

import com.ibm.xsp.ajax.AjaxUtil;
import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.resource.ScriptResource;

/**
 * the basic zk component renderer, it handle 4 cases of jsf-life cycle.
 * 1. direct link, 2. postback, 3. ajax-postback, 4. ajax-postback but no in refresh area. 
 * @author Dennis Chen
 *
 */
public abstract class ZulRendererBase extends javax.faces.render.Renderer {

	abstract protected Component createZKComponent(Page page, ZulComponentBase zcomp);

	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		if (!(component instanceof ZulComponentBase)) {
			throw new IllegalArgumentException("unsupported component " + component);
		}
		ZulComponentBase zcomp = (ZulComponentBase) component;
		ResponseWriter writer = context.getResponseWriter();
		if (zcomp.isDesktopTimeout()) {
			// TODO
		} else {
			JsfContext jsfc = JsfContext.instance();
			if(!jsfc.isPostback()){
				//direct link
				startBodyTag(context,writer,component);
				//new a fake page, so all zk script and css will be generated here
				newFakePage(zcomp,context);
				
				startJsTag(writer);
				//invoke relocate css to head in client side
				writer.write("zkXPage.relocateCss('#"+zcomp.getClientId(context)+"');");
				endJsTag(writer);
				endBodyTag(context,writer,component);
				
				//create a new zk component and attach it to new page
				newZKComponent(zcomp, context);

				
			}else if(!jsfc.isAjaxPartialRefresh()){
				//full postback, attach to new desktop
				startBodyTag(context,writer,component);
				
				newFakePage(zcomp,context);

				startJsTag(writer);
				//relocate css in fake page
				writer.write("zkXPage.relocateCss('#"+zcomp.getClientId(context)+"');");
				endJsTag(writer);
				endBodyTag(context,writer,component);
				
				//reuse old zk component and attach it to new page
				reattachZKComponent(zcomp, context);

				
			}else if(jsfc.isAjaxRendered(component)){
				//ajax-postback
				startBodyTag(context,writer,component);
				endBodyTag(context,writer,component);

				//add au
				outXspUpdateJs(zcomp.getAuScripts());
	            zcomp.clearAuScripts();
	            //reattach client widget to new dom (old one was replaced by new one with sam id)
	            outXspJs("zkXPage.reattach('$" + zcomp.getComponent().getId() + "','#"+zcomp.getClientId(context)+"');");

			}else if(zcomp.hasAuScript()){
				// ajax-postback but no in refresh area. 
				//add au
				outXspUpdateJs(zcomp.getAuScripts());
	            zcomp.clearAuScripts();
			}
		}
	}
	@SuppressWarnings("unchecked")
	protected void outXspJs(String script){
		List al =new ArrayList();
		al.add(script);
		outXspUpdateJs(al);
	}
	
	/*
	 *  out javascript for a ajax-post back,
	 *  the js will be rendered in the end of response content , wrapped by 
	 *  <!-- XSP_UPDATE_HEADER_START --> ... <!-- XSP_UPDATE_HEADER_END -->
	 */
	@SuppressWarnings("unchecked")
	protected void outXspUpdateJs(List scripts){
		if(scripts.size()==0) return;
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean isRendering = AjaxUtil.isRendering(FacesContext.getCurrentInstance());
        AjaxUtil.setRendering(ctx, true);
        try {
        	UIViewRootEx vrex = ((UIViewRootEx)ctx.getViewRoot());
        	Iterator iter = scripts.iterator();
			while(iter.hasNext()){
				String script = (String)iter.next();
				ScriptResource r = new ScriptResource();
                r.setClientSide(true);
                r.setContents(script);
                vrex.addEncodeResource(r);
			}
        } finally {
            AjaxUtil.setRendering(ctx, isRendering);
        }
	}

	protected void newFakePage(final ZulComponentBase zcomp, FacesContext context) throws IOException {
		final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		final HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		final ServletContext svlctx = (ServletContext) context.getExternalContext().getContext();
		final ResponseWriter writer = context.getResponseWriter();
		try {
			Renders.render(svlctx, request, response, new GenericRichlet() {
				public void service(Page page) throws Exception {
					//do nothing, it is just a skelton for zk js and css
				}
			}, null, writer);
		} catch (ServletException e) {
			Log.error(this, e.getMessage(), e);
			throw new IOException(e.getMessage());
		}
	}
	
	protected void newZKComponent(final ZulComponentBase zcomp, FacesContext context) throws IOException {
		final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		final HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		final ServletContext svlctx = (ServletContext) context.getExternalContext().getContext();
		final ResponseWriter writer = context.getResponseWriter();
		try {
			
			Renders.render(svlctx, request, response, new GenericRichlet() {
				public void service(Page page) throws Exception {
					Component comp = createZKComponent(page, zcomp);
					comp.setId(zcomp.getId());
					comp.setPage(page);
					applyAttributes(zcomp,comp);
					applyComposer(zcomp,comp);
					zcomp.setComponent(comp);
				}
			}, null, writer);
		} catch (ServletException e) {
			Log.error(this, e.getMessage(), e);
			throw new IOException(e.getMessage());
		}
		
	}

	protected void applyAttributes(ZulComponentBase zcomp, Component comp) throws Exception {
		if(comp instanceof XulElement){
			XulElement xul = (XulElement)comp;
			String s ;
			s = zcomp.getStyle();
			if(s!=null){
				xul.setStyle(zcomp.getStyle());
			}
			s = zcomp.getSclass();
			if(s!=null){
			xul.setSclass(s);
			}
			s = zcomp.getZclass();
			if(s!=null){
				xul.setZclass(s);
			}
			Integer zi = zcomp.getZindex();
			if(zi!=null){
				xul.setZindex(zi.intValue());
			}
			
			s = zcomp.getHeight();
			if(s!=null){
				xul.setHeight(s);
			}
			s = zcomp.getWidth();
			if(s!=null){
				xul.setWidth(s);
			}
			s = zcomp.getHflex();
			if(s!=null){
				xul.setHflex(s);
			}
			s = zcomp.getVflex();
			if(s!=null){
				xul.setVflex(s);
			}
		}
		
	}

	protected void reattachZKComponent(final ZulComponentBase zcomp, FacesContext context)
			throws IOException {
		Desktop dt = zcomp.getDesktop();
		if (dt == null) {
			throw new IllegalStateException("desktop not found");
		}
		final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		final HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		final ServletContext svlctx = (ServletContext) context.getExternalContext().getContext();
		final ResponseWriter writer = context.getResponseWriter();
		try {
			Bridge bridge = Bridge.start(svlctx, request, response,dt);
			try {
				//detach
				zcomp.getComponent().detach();	
			} finally {
				bridge.close();
			}
			//reassign
			Renders.render(svlctx, request, response, new GenericRichlet() {
				public void service(Page page) throws Exception {
					Component comp = zcomp.getComponent();
					comp.setPage(page);
					zcomp.setComponent(comp);
				}
			}, null, writer);
		} catch (ServletException e) {
			Log.error(this, e.getMessage(), e);
			throw new IOException(e.getMessage());
		}
	}
	
	protected void applyComposer(final ZulComponentBase zcomp, final Component comp) throws Exception{
		Object o = zcomp.getApply();
		if(o instanceof String) {
			o = Classes.newInstanceByThread(o.toString());
		}
		if(o instanceof Composer) {
			((Composer)o).doAfterCompose(comp);
		}
	}
	
	protected String getBodyTag(){
		return "div";
	}

	protected void startBodyTag(FacesContext context,ResponseWriter writer,UIComponent component) throws IOException {
		writer.startElement(getBodyTag(), component);
		writer.writeAttribute("id", component.getClientId(context), null);
		writer.writeAttribute("style", "display:none", null);
	}
	
	protected void endBodyTag(FacesContext context,ResponseWriter writer,UIComponent component) throws IOException {
		writer.endElement(getBodyTag());
	}
	
	protected void startJsTag(ResponseWriter writer) throws IOException {
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
	}

	protected void endJsTag(ResponseWriter writer) throws IOException {
		writer.endElement("script");
	}
	
	
}
