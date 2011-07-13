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
package org.zkoss.xpage.core.web;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class ServletConfigAdapter implements ServletConfig{
	
	protected ServletConfig config;
	protected String zkau = "/zkau";
	protected boolean compress = false;
	
	public ServletConfigAdapter(ServletConfig config){
		this.config = config;
	}
	
	public String getInitParameter(String parm) {
		if("update-uri".equals(parm)){
			return zkau;
		}else if("compress".equals(parm)){
			return Boolean.toString(compress);
		}
		return config.getInitParameter(parm);
	}
	public Enumeration getInitParameterNames() {
		return config.getInitParameterNames();
	}

	public ServletContext getServletContext() {
		return config.getServletContext();
	}

	public String getServletName() {
		return config.getServletName();
	}
}
