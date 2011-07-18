package org.zkoss.xpage.core.renderkit.html_basic;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.xpage.core.component.ZKComponentBase;
import org.zkoss.xpage.core.util.Log;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;
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
			Component comp = zcomp.getComponent();
			//need to know, it is postback, ajax-postback, contained in a ajax-postback refresh
			boolean invalidate  = comp!=null;//how to now invalidate or create new?
			
			writer.startElement("div", component);
			writer.writeAttribute("id", component.getClientId(context), null);
			if (invalidate) {
				// TODO don't know how to make it work
//				try {
//					
//					doInvalidZKComponent(zcomp, context, component.getClientId(context));
//				} catch (ServletException e) {
//					Log.error(this, e.getMessage(), e);
//					throw new IOException(e.getMessage());
//				}
			} else {
				try {
					doCreateZKComponent(zcomp, context);
				} catch (ServletException e) {
					Log.error(this, e.getMessage(), e);
					throw new IOException(e.getMessage());
				}
				
			}
			writer.endElement("div");
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
				zcomp.setComponent(comp);
			}
		}, null, writer);
	}

	protected void doInvalidZKComponent(final ZKComponentBase zcomp, FacesContext context, final String hookid)
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
		startJs(writer);
		writer.write("zkXPage.detach('$" + zcomp.getComponent().getId() + "');");//must be component id
		endJs(writer);
		
		//write a fake widget dom what will be 
		writer.startElement("div", null);
		writer.writeAttribute("id", zcomp.getComponent().getUuid(), null);
		writer.endElement("div");
		
		Bridge bridge = Bridge.start(svlctx, request, response, dt);
		try {
			zcomp.getComponent().invalidate();
			String auResult = bridge.getResult();
			startJs(writer);	
			//recreate new one on the fake
			writer.write(auResult);
			endJs(writer);
		} finally {
			bridge.close();
		}
	}

	protected void startJs(ResponseWriter writer) throws IOException {
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
	}

	protected void endJs(ResponseWriter writer) throws IOException {
		writer.endElement("script");
	}
}
