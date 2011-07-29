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
package org.zkoss.xpage.core.renderkit.zul;

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
import org.zkoss.xpage.core.component.ZulBridgeBase;
import org.zkoss.xpage.core.util.Log;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zkplus.embed.Bridge;
import org.zkoss.zkplus.embed.Renders;

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

	abstract protected HtmlBasedComponent createRootComponent(Page page, ZulBridgeBase bridge);

	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		if (!(component instanceof ZulBridgeBase)) {
			throw new IllegalArgumentException("unsupported component " + component);
		}
		ZulBridgeBase bridge = (ZulBridgeBase) component;
		ResponseWriter writer = context.getResponseWriter();
		if (bridge.isDesktopTimeout()) {
			// TODO
		} else {
			JsfContext jsfc = JsfContext.instance();
			if(!jsfc.isPostback()){
				//direct link
				startBodyTag(context,writer,component);
				//new a fake page, so all zk script and css will be generated here
				newFakePage(bridge,context);
				
				startJsTag(writer);
				//invoke relocate css to head in client side
				writer.write("zkXPage.relocateCss('#"+bridge.getClientId(context)+"');");
				endJsTag(writer);
				endBodyTag(context,writer,component);
				
				//create a new zk component and attach it to new page
				newZKComponent(bridge, context);

				
			}else if(!jsfc.isAjaxPartialRefresh()){
				//full postback, attach to new desktop
				startBodyTag(context,writer,component);
				
				newFakePage(bridge,context);

				startJsTag(writer);
				//relocate css in fake page
				writer.write("zkXPage.relocateCss('#"+bridge.getClientId(context)+"');");
				endJsTag(writer);
				endBodyTag(context,writer,component);
				
				//reuse old zk component and attach it to new page
				reattachZKComponent(bridge, context);

				
			}else if(jsfc.isAjaxRendered(component)){
				//ajax-postback
				startBodyTag(context,writer,component);
				endBodyTag(context,writer,component);

				//add au
				outXspUpdateJs(bridge.getAuScripts());
	            bridge.clearAuScripts();
	            //reattach client widget to new dom (old one was replaced by new one with sam id)
	            outXspJs("zkXPage.reattach('$" + bridge.getComponent().getId() + "','#"+bridge.getClientId(context)+"');");

			}else if(bridge.hasAuScript()){
				// ajax-postback but no in refresh area. 
				//add au
				outXspUpdateJs(bridge.getAuScripts());
	            bridge.clearAuScripts();
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

	protected void newFakePage(final ZulBridgeBase bridge, FacesContext context) throws IOException {
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
	
	protected void newZKComponent(final ZulBridgeBase bridge, final FacesContext context) throws IOException {
		final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		final HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		final ServletContext svlctx = (ServletContext) context.getExternalContext().getContext();
		final ResponseWriter writer = context.getResponseWriter();
		try {
			Renders.render(svlctx, request, response, new GenericRichlet() {
				public void service(Page page) throws Exception {
					
					applyVariableResolver(page);
					
					HtmlBasedComponent comp = createRootComponent(page, bridge);
					comp.setId(bridge.getId());
					comp.setPage(page);
					bridge.setComponent(comp);
					
					applyAttributes(bridge,comp);
					applyComposer(bridge,comp,context);
					afterComposer(bridge,comp,context);
					
				}
			}, null, writer);
		} catch (ServletException e) {
			Log.error(this, e.getMessage(), e);
			throw new IOException(e.getMessage());
		}
		
	}

	protected void applyVariableResolver(Page page) {
		//I canot apply variableresolver to page, because in incoming au, it will resolve nothing, since the bean is only in facescontext not in au
    }

	protected void applyAttributes(ZulBridgeBase bridge, HtmlBasedComponent comp) throws Exception {
		String s;
		s = bridge.getStyle();
		if (s != null) {
			comp.setStyle(bridge.getStyle());
		}
		s = bridge.getSclass();
		if (s != null) {
			comp.setSclass(s);
		}
		s = bridge.getZclass();
		if (s != null) {
			comp.setZclass(s);
		}
		Integer zi = bridge.getZindex();
		if (zi != null) {
			comp.setZindex(zi.intValue());
		}

		s = bridge.getHeight();
		if (s != null) {
			comp.setHeight(s);
		}
		s = bridge.getWidth();
		if (s != null) {
			comp.setWidth(s);
		}
		s = bridge.getHflex();
		if (s != null) {
			comp.setHflex(s);
		}
		s = bridge.getVflex();
		if (s != null) {
			comp.setVflex(s);
		}

	}

	protected void reattachZKComponent(final ZulBridgeBase bridge, FacesContext context)
			throws IOException {
		Desktop dt = bridge.getDesktop();
		if (dt == null) {
			throw new IllegalStateException("desktop not found");
		}
		final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		final HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		final ServletContext svlctx = (ServletContext) context.getExternalContext().getContext();
		final ResponseWriter writer = context.getResponseWriter();
		try {
			Bridge zbridge = Bridge.start(svlctx, request, response,dt);
			try {
				//detach
				bridge.getComponent().detach();	
				//don't need to out au, all page is reloaded
			} finally {
				zbridge.close();
			}
			//reassign
			Renders.render(svlctx, request, response, new GenericRichlet() {
				public void service(Page page) throws Exception {
					Component comp = bridge.getComponent();
					comp.setPage(page);
					bridge.setComponent(comp);
				}
			}, null, writer);
		} catch (ServletException e) {
			Log.error(this, e.getMessage(), e);
			throw new IOException(e.getMessage());
		}
	}
	
	protected void afterComposer(final ZulBridgeBase bridge, final HtmlBasedComponent comp,final FacesContext context) throws Exception{
		
	}
	protected void applyComposer(final ZulBridgeBase bridge, final HtmlBasedComponent comp, final FacesContext context) throws Exception{
		Object o = bridge.getApply();
		if(o == null) return;
		Object composer = null;
		if(o instanceof String) {
			composer = Classes.newInstanceByThread(o.toString());
		}
		if(composer instanceof Composer) {
			((Composer)composer).doAfterCompose(comp);
		}else if(composer instanceof org.zkoss.xpage.core.util.Composer) {
			((org.zkoss.xpage.core.util.Composer)composer).doAfterCompose(bridge,context);
		}else{
			throw new IllegalArgumentException("illegal composer "+composer +" is not "+Composer.class);
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
