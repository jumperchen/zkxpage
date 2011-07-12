package org.zkoss.xpage.core.component;

import javax.faces.component.UIComponentBase;

public class ButtonControl extends UIComponentBase {
	public ButtonControl() {
		super();
		setRendererType("org.zkoss.xpage.buttoncontrol");
	}

	@Override
	public String getFamily() {
		return "org.zkoss.xpage";
	}

}
