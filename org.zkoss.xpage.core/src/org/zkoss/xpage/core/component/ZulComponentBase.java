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
package org.zkoss.xpage.core.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.lang.Strings;
import org.zkoss.xpage.core.util.ZkUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zkplus.embed.Bridge;
/**
 * 
 * @author Dennis Chen
 *
 */
public abstract class ZulComponentBase extends UIComponentBase implements Serializable {
	private static final long serialVersionUID = 1L;

	/* zk component instance, will be assign by renderer */
	transient Component component;
	/* is desktop already timeout */
	transient boolean desktopTimeout;
	
	@SuppressWarnings("unchecked")
	transient List auScripts = new ArrayList();
	
	private Object apply = null;
	protected StateValueMap states = new StateValueMap();
	
	
	
	@SuppressWarnings("unchecked")
	public void addAuScript(String script) {
		auScripts.add(script);
	}

	@SuppressWarnings("unchecked")
	public List getAuScripts() {
		return Collections.unmodifiableList(auScripts);
	}

	public void clearAuScripts() {
		auScripts.clear();
	}

	public boolean hasAuScript() {
		return auScripts.size()>0;
	}


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

	/** get ZK Component that was created by this ZK JSF Component**/
	public Component getComponent() {
		return component;
	}

	/** sets ZK component, you should never set this, this is only for renderer **/
	public void setComponent(Component component) {
		this.component = component;
	}

	/** get ZK Desktop that relates to component **/ 
	public Desktop getDesktop() {
		return component == null ? null : component.getDesktop();
	}

	/** is desktop already timeout **/
	public boolean isDesktopTimeout() {
		return desktopTimeout;
	}


	public String getStyle() {
		return states.getString("style",getFacesContext(),this);
	}

	public void setStyle(String style) {
		states.set("style", style);
	}

	public String getSclass() {
		return states.getString("sclass",getFacesContext(),this);
	}

	public void setSclass(String sclass) {
		states.set("sclass", sclass);
	}

	public String getZclass() {
		return states.getString("zclass",getFacesContext(),this);
	}

	public void setZclass(String zclass) {
		states.set("zclass", zclass);
	}

	public Integer getZindex() {
		return states.getInteger("zindex",getFacesContext(),this);
	}

	public void setZindex(Integer zindex) {
		states.set("zindex", zindex);
	}

	public String getWidth() {
		return states.getString("width",getFacesContext(),this);
	}

	public void setWidth(String width) {
		states.set("width", width);
	}

	public String getHeight() {
		return states.getString("height",getFacesContext(),this);
	}

	public void setHeight(String height) {
		states.set("height", height);
	}

	public String getHflex() {
		return states.getString("hflex",getFacesContext(),this);
	}

	public void setHflex(String hflex) {
		states.set("hflex", hflex);
	}

	public String getVflex() {
		return states.getString("vflex",getFacesContext(),this);
	}

	public void setVflex(String vfex) {
		states.set("vflex", vfex);
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		states = (StateValueMap)values[1];
		String uuid = (String) values[2];
		String dtid = (String) values[3];
		ServletContext servletContext = (ServletContext) context
				.getExternalContext().getContext();
		HttpServletRequest req = (HttpServletRequest) context
				.getExternalContext().getRequest();
		Desktop desktop = ZkUtil.findDesktop(servletContext, req, dtid);
		if (desktop != null) {
			component = ZkUtil.findComponent(desktop, uuid);
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

	/**
	 * Execute the Action, in the {@link Action#doAction()} you can access ZK component by {@link ZulComponentBase#getComponent()},
	 * and any zk update automatically update to client side
	 * @param action
	 */
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
			action.doAction(this);
			String auResult = bridge.getResult();
			if(!Strings.isBlank(auResult)){
				addAuScript(auResult);
			}
		} finally {
			bridge.close();
		}
	}
	
	@Override
	public boolean getRendersChildren() {
		/* zk component should control children rendering. which means, not allow children to be rendered by default*/
		return true;
	}

}
