package org.zkoss.xpage.zss.component;

import javax.faces.component.UIComponentBase;

public class Spreadsheet extends UIComponentBase {
	public Spreadsheet() {
		super();
		setRendererType("org.zkoss.xpage.spreadsheet");
	}

	@Override
	public String getFamily() {
		return "org.zkoss.xpage";
	}

}
