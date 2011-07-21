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
import org.zkoss.lang.Strings;
import org.zkoss.xpage.core.bean.JsfContext;
import org.zkoss.xpage.core.component.ZKComponentBase;
import org.zkoss.xpage.core.util.Log;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zkplus.embed.Bridge;
import org.zkoss.zkplus.embed.Renders;

import com.ibm.xsp.ajax.AjaxUtil;
import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.resource.ScriptResource;

public abstract class ZKRendererBase extends javax.faces.render.Renderer {

	abstract protected Component createZKComponent(Page page, ZKComponentBase zcomp);

	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		if (!(component instanceof ZKComponentBase)) {
			throw new IllegalArgumentException("unsupported component " + component);
		}
		ZKComponentBase zcomp = (ZKComponentBase) component;
		ResponseWriter writer = context.getResponseWriter();
		if (zcomp.isDesktopTimeout()) {
			// TODO
		} else {
			JsfContext jsfc = JsfContext.instance();
			if(!jsfc.isPostback()){
				//direct link or full update
				startBodyTag(context,writer,component);
				try {
					newZKComponent(zcomp, context);
				} catch (ServletException e) {
					Log.error(this, e.getMessage(), e);
					throw new IOException(e.getMessage());
				}
				startJsTag(writer);
				writer.write("zkXPage.relocateCss('#"+zcomp.getClientId(context)+"');");
				endJsTag(writer);
				endBodyTag(context,writer,component);
			}else if(!jsfc.isAjaxPartialRefresh()){
				//full refresh
				//direct link or full update
				startBodyTag(context,writer,component);
				try {
					reattachZKComponent(zcomp, context);
				} catch (ServletException e) {
					Log.error(this, e.getMessage(), e);
					throw new IOException(e.getMessage());
				}
				startJsTag(writer);
				writer.write("zkXPage.relocateCss('#"+zcomp.getClientId(context)+"');");
				endJsTag(writer);
				endBodyTag(context,writer,component);
				
			}else if(jsfc.isAjaxRendered(component)){
				startBodyTag(context,writer,component);
				endBodyTag(context,writer,component);
				
				
				//add au
				outXspUpdateJs(zcomp.getScripts());
	            zcomp.clearScripts();
	            //reattach to new dom (same id)
	            outXspJs("zkXPage.reattach('$" + zcomp.getComponent().getId() + "','#"+zcomp.getClientId(context)+"');");
	            
				
			}else if(zcomp.hasScript()){
				//add au
				outXspUpdateJs(zcomp.getScripts());
	            zcomp.clearScripts();
			}
		}
	}
	protected void outXspJs(String script){
		List al =new ArrayList();
		al.add(script);
		outXspUpdateJs(al);
	}
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

	protected void newZKComponent(final ZKComponentBase zcomp, FacesContext context) throws ServletException,
			IOException {
		final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		final HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		final ServletContext svlctx = (ServletContext) context.getExternalContext().getContext();
		final ResponseWriter writer = context.getResponseWriter();
		Renders.render(svlctx, request, response, new GenericRichlet() {
			public void service(Page page) throws Exception {
				Component comp = createZKComponent(page, zcomp);
				comp.setPage(page);
//				applyDynamicAttributes(zcomp,comp);
				applyComposer(zcomp,comp);
				zcomp.setComponent(comp);
			}
		}, null, writer);
	}

//	protected void applyDynamicAttributes(ZKComponentBase zcomp, Component comp) throws Exception {
//		//TODO how? not supported in domino
//	}

	protected void reattachZKComponent(final ZKComponentBase zcomp, FacesContext context)
			throws ServletException, IOException {
		Desktop dt = zcomp.getDesktop();
		if (dt == null) {
			throw new IllegalStateException("desktop not found");
		}

		final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		final HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		final ServletContext svlctx = (ServletContext) context.getExternalContext().getContext();
		final ResponseWriter writer = context.getResponseWriter();
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
	}
	
	protected void applyComposer(final ZKComponentBase zcomp, final Component comp) throws Exception{
		Object o = zcomp.getApply();
		if(o instanceof String) {
			o = Classes.newInstanceByThread(o.toString());
		}
		if(o instanceof Composer) {
			((Composer)o).doAfterCompose(comp);
		}
	}

	protected void startBodyTag(FacesContext context,ResponseWriter writer,UIComponent component) throws IOException {
		writer.startElement("div", component);
		writer.writeAttribute("id", component.getClientId(context), null);
	}
	
	protected void endBodyTag(FacesContext context,ResponseWriter writer,UIComponent component) throws IOException {
		writer.endElement("div");
	}
	
	protected void startJsTag(ResponseWriter writer) throws IOException {
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
	}

	protected void endJsTag(ResponseWriter writer) throws IOException {
		writer.endElement("script");
	}
	
	
}
