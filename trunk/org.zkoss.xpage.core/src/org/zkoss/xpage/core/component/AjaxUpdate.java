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

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.zkoss.xpage.core.Constants;

import com.ibm.xsp.component.FacesAjaxComponent;


public class AjaxUpdate extends UIComponentBase{
	private static final long serialVersionUID = 1L;

	public AjaxUpdate() {
		super();
		setRendererType("org.zkoss.xpage.ajaxUpdate");
	}

	@Override
	public String getFamily() {
		return Constants.COMPONENT_FAMILY;
	}
}
