package org.zkoss.xpage.core.renderkit.html_basic;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.lang.Classes;
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
			if(!jsfc.isPostback() || !jsfc.isAjaxPartialRefresh()){
				startBodyTag(context,writer,component);
				try {
					doCreateZKComponent(zcomp, context);
				} catch (ServletException e) {
					Log.error(this, e.getMessage(), e);
					throw new IOException(e.getMessage());
				}
				endBodyTag(context,writer,component);
			}else if(jsfc.isAjaxRendered(component)){
				startBodyTag(context,writer,component);
				try {
					doRecreateZKComponent(zcomp, context);
				} catch (ServletException e) {
					Log.error(this, e.getMessage(), e);
					throw new IOException(e.getMessage());
				}
				endBodyTag(context,writer,component);
			}else{
				//nothing.
			}
		}
	}

	protected void doCreateZKComponent(final ZKComponentBase zcomp, FacesContext context) throws ServletException,
			IOException {
		final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		final HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		final ServletContext svlctx = (ServletContext) context.getExternalContext().getContext();
		final ResponseWriter writer = context.getResponseWriter();
		Renders.render(svlctx, request, response, new GenericRichlet() {
			public void service(Page page) throws Exception {
				Component comp = createZKComponent(page, zcomp);
				comp.setPage(page);
				applyDynamicAttributes(zcomp,comp);
				applyComposer(zcomp,comp);
				zcomp.setComponent(comp);
			}
		}, null, writer);
	}

	protected void applyDynamicAttributes(ZKComponentBase zcomp, Component comp) throws Exception {
		//TODO how?
	}

	protected void doRecreateZKComponent(final ZKComponentBase zcomp, FacesContext context)
			throws ServletException, IOException {
		Desktop dt = zcomp.getDesktop();
		if (dt == null) {
			throw new IllegalStateException("desktop not found");
		}

		final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		final HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		final ServletContext svlctx = (ServletContext) context.getExternalContext().getContext();
		final ResponseWriter writer = context.getResponseWriter();
		
		//detach the old one, the dom is not exsit already here
		startJsTag(writer);
		//TODO need to remove desktop if there is only one
		writer.write("zkXPage.detach('$" + zcomp.getComponent().getId() + "');");//must be component id
		endJsTag(writer);
		zcomp.setComponent(null);
		doCreateZKComponent(zcomp,context);
		
//		//write a fake widget dom what will be 
//		writer.startElement("div", null);
//		writer.writeAttribute("id", zcomp.getComponent().getUuid(), null);
//		writer.endElement("div");
//		
//		Bridge bridge = Bridge.start(svlctx, request, response, dt);
//		try {
//			zcomp.getComponent().invalidate();
//			String auResult = bridge.getResult();
//			startJs(writer);	
//			//recreate new one on the fake
//			writer.write(auResult);
//			endJs(writer);
//		} finally {
//			bridge.close();
//		}
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
