package org.zkoss.xpage.core.component;

import java.io.Serializable;

import javax.faces.component.UIComponentBase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.lang.Strings;
import org.zkoss.xpage.core.bean.AuContext;
import org.zkoss.xpage.core.util.DesktopUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zkplus.embed.Bridge;

public abstract class ZKComponentBase extends UIComponentBase implements
		ZKComponent, Serializable {
	private static final long serialVersionUID = 1L;

	transient Component component;
	transient boolean desktopTimeout;
	
	Object apply = null;
	StateValueMap states = new StateValueMap();


	public Object getApply() {
		if(apply!=null){
			return apply;
		}
		ValueBinding vb = getValueBinding("apply");
		if(vb!=null){
			return vb.getValue(getFacesContext());
		}
		return null;
	}

	public void setApply(Object apply) {
		this.apply = apply;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public Desktop getDesktop() {
		return component == null ? null : component.getDesktop();
	}

	public boolean isDesktopTimeout() {
		return desktopTimeout;
	}


	public String getStyle() {
		return states.getString("style");
	}

	public void setStyle(String style) {
		states.set("style", style);
	}

	public String getSclass() {
		return states.getString("sclass");
	}

	public void setSclass(String sclass) {
		states.set("sclass", sclass);
	}

	public String getZclass() {
		return states.getString("zclass");
	}

	public void setZclass(String zclass) {
		states.set("zclass", zclass);
	}

	public int getZindex() {
		Integer z = states.getInteger("zindex");
		return z==null?-1:z.intValue();
	}

	public void setZindex(int zindex) {
		states.set("zindex", zindex);
	}

	public String getWidth() {
		return states.getString("width");
	}

	public void setWidth(String width) {
		states.set("width", width);
	}

	public String getHeight() {
		return states.getString("height");
	}

	public void setHeight(String height) {
		states.set("height", height);
	}

	public String getHflex() {
		return states.getString("hflex");
	}

	public void setHflex(String hflex) {
		states.set("hflex", hflex);
	}

	public String getVfex() {
		return states.getString("vfex");
	}

	public void setVfex(String vfex) {
		states.set("vfex", vfex);
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		states = (StateValueMap)values[1];
		
		String uuid = (String) values[2];

		// not work, this is new Desktop, not the original one, and after get
		// component from this desktop, the component.getDesktop is always null
		// Desktop desktop = (Desktop)values[2]
		String dtid = (String) values[3];
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
		
		apply = values[4];
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

		Object state[] = new Object[5];
		state[0] = super.saveState(context);
		state[1] = states;
		
		state[2] = component.getUuid();
		// state[2] = dt;//not work, state will be serialized and deserialized,
		// after reference between desktop and component is lost.
		state[3] = getDesktop().getId();
		state[4] = apply instanceof String?(String)apply:null;
		
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
				AuContext.instance().addScript(auResult);
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
