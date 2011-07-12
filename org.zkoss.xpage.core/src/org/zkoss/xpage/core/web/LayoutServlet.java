package org.zkoss.xpage.core.web;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.zkoss.zk.ui.http.DHtmlLayoutServlet;

public class LayoutServlet extends DHtmlLayoutServlet {
	private static final long serialVersionUID = 1L;
	
	public void init(final ServletConfig config) throws ServletException {
		Log.log(this,"init::" + this+",context = "+config.getServletContext());
		config.getServletContext().setAttribute("att1", "AAAAAAAAAAAAA");
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(LayoutServlet.class.getClassLoader());
			super.init(new ServletConfigAdapter(config));
		}catch(Exception x){
			Log.error(this,x.getMessage(),x);
			throw new ServletException(x.getMessage(),x);
		}finally{
			Thread.currentThread().setContextClassLoader(cl);
		}
		Log.log(this,"init end::" + this);
	}

}
