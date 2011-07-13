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

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.util.logging.Log;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.http.WebManager;

public class MixedAuServlet /* extends HttpServlet {// */extends DHtmlUpdateServlet {
	private static final long serialVersionUID = 1L;
	private static final Log log = Log.lookup(MixedAuServlet.class);
	
	
	LayoutServlet layoutServlet;

	public void init(final ServletConfig config) throws ServletException {
		log.warning("MixedAuServlet.init::" + this+",context = "+config.getServletContext());
		layoutServlet = new LayoutServlet();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(MixedAuServlet.class.getClassLoader());
			layoutServlet.init(config);
			log.warning(">MixedAuServlet.init:: att1 = "+config.getServletContext().getAttribute("att1"));
			super.init(new ServletConfig(){
				public String getInitParameter(String parm) {
					if("compress".equals(parm)){
						//Domino overrid all the header, we trun compress off here.
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
				}});
		} catch (Exception x) {
			log.error(x.getMessage(), x);
			throw new ServletException(x.getMessage(), x);
		}finally{
			Thread.currentThread().setContextClassLoader(cl);
		}
		log.warning("MixedAuServlet init end::" + this);
	}
	
	

	@Override
	public void destroy() {
		super.destroy();
		layoutServlet.destroy();
		layoutServlet = null;
	}


	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.warning("MixedAuServlet doGet::" + this+", servletPath "+request.getServletPath()+", pathInfo:"+request.getPathInfo());
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(MixedAuServlet.class.getClassLoader());
			
			String dtid = request.getParameter("dtid");
			final Session sess = WebManager.getSession(getServletContext(), request, false);
			Desktop dt = getDesktop(sess,dtid);
			
			log.warning("MixedAuServlet session="+request.getSession().getId()+", dtid="+dtid+",desktop = "+dt+",session_attr1::" + request.getSession().getAttribute("session_attr1"));
						
			
			super.doGet(request, response);
		} catch (ServletException x) {
			log.error(x.getMessage(),x);
			throw x;
		} catch (IOException x) {
			log.error(x.getMessage(),x);
			throw x;
		} catch (Exception x) {
			log.error(x.getMessage(),x);
			throw new ServletException(x.getMessage(),x);
		}finally{
			Thread.currentThread().setContextClassLoader(cl);
		}
		log.warning("MixedAuServlet doGet End::" + this);
		// PrintWriter pw = response.getWriter();
		// pw.append("this is au update servlet " + this);
		// pw.flush();

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
