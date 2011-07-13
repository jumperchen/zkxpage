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
package org.zkoss.xpage.core.component;

import javax.faces.component.UIComponentBase;

public class Verifier extends UIComponentBase {
	public Verifier() {
		super();
		setRendererType("org.zkoss.xpage.verifier");
	}

	@Override
	public String getFamily() {
		return "org.zkoss.xpage";
	}

}
