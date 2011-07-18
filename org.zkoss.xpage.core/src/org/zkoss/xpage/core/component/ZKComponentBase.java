package org.zkoss.xpage.core.component;

import java.io.Serializable;

import javax.faces.component.UIComponentBase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.lang.Strings;
import org.zkoss.xpage.core.bean.AuResult;
import org.zkoss.xpage.core.util.DesktopUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zkplus.embed.Bridge;

public abstract class ZKComponentBase extends UIComponentBase implements
		ZKComponent, Serializable {
	private static final long serialVersionUID = 1L;

	transient Component component;
	transient boolean desktopTimeout;

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
		;
	}

	public Desktop getDesktop() {
		return component == null ? null : component.getDesktop();
	}

	public boolean isDesktopTimeout() {
		return desktopTimeout;
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);

		String uuid = (String) values[1];

		// not work, this is new Desktop, not the original one, and after get
		// component from this desktop, the component.getDesktop is always null
		// Desktop desktop = (Desktop)values[2]
		String dtid = (String) values[2];
		ServletContext servletContext = (ServletContext) context
				.getExternalContext().getContext();
		HttpServletRequest req = (HttpServletRequest) context
				.getExternalContext().getRequest();
		Desktop desktop = DesktopUtil.findDesktop(servletContext, req, dtid);
		if (desktop != null) {
			component = DesktopUtil.findComponent(desktop, uuid);
			if (component == null) {
				throw new IllegalStateException("component " + uuid
						+ " not in desktop " + desktop);
			}
		} else {
			desktopTimeout = true;
		}
	}

	@Override
	public Object saveState(FacesContext context) {
		if (component == null) {
			throw new IllegalStateException("component not found");
		}
		Desktop dt = component.getDesktop();
		if (dt == null) {
			throw new IllegalStateException("desktop of component " + component
					+ " not found");
		}

		Object state[] = new Object[3];
		state[0] = super.saveState(context);
		state[1] = component.getUuid();
		// state[2] = dt;//not work, state will be serialized and deserialized,
		// after reference between desktop and component is lost.
		state[2] = getDesktop().getId();
		return state;
	}

	public void execute(Action action) {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ServletContext svlctx = (ServletContext) ec.getContext();
		HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		HttpServletResponse response = (HttpServletResponse) ec.getResponse();

		Desktop dt = getDesktop();
		
		if(dt==null){
			throw new IllegalStateException("desktop not found");
		}

		Bridge bridge = Bridge.start(svlctx, request, response,dt);
		try {
			action.doAction();
			String auResult = bridge.getResult();
			if(!Strings.isBlank(auResult)){
				AuResult.instance().addScript(auResult);
			}
		} finally {
			bridge.close();
		}
	}
	
	@Override
	public boolean getRendersChildren() {
		/* zk component should control children rendering.*/
		return true;
	}

}
