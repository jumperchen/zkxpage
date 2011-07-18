/* 
{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		July 13, 2011 , Created by dennischen
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.xpage.zss.component;

import org.zkoss.xpage.core.component.ZKComponentBase;

public class Spreadsheet extends ZKComponentBase {

	private static final long serialVersionUID = 1L;

	public Spreadsheet() {
		super();
		setRendererType("org.zkoss.xpage.spreadsheet");
	}

	@Override
	public String getFamily() {
		return "org.zkoss.xpage";
	}

}
