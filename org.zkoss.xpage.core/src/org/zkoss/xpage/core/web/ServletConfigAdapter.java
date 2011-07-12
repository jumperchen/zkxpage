package org.zkoss.xpage.core.web;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class ServletConfigAdapter implements ServletConfig{
	
	private ServletConfig config;
	
	public ServletConfigAdapter(ServletConfig config){
		this.config = config;
	}
	
	public String getInitParameter(String parm) {
		if("update-uri".equals(parm)){
			return "/zkau";
		}else if("compress".equals(parm)){
			return "false";
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
