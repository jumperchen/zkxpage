package org.zkoss.xpage.core.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.lang.Library;
import org.zkoss.xpage.core.web.AuServlet;
import org.zkoss.xpage.core.web.Log;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.DHtmlLayoutServlet;

import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.IServletFactory;
import com.ibm.designer.runtime.domino.adapter.ServletMatch;

public class XspServletFactory implements IServletFactory {


	private static final org.zkoss.util.logging.Log log = org.zkoss.util.logging.Log.lookup(XspServletFactory.class);
	/** see org.zkoss.zkplus.embed.Renders **/
	static final String EMBED_AU_URI = "org.zkoss.zkplus.embed.updateURI";
	static final String XSP_AU = "/xsp/zkau";
	static final String AU = "/zkau";
	static{
		Library.setProperty(EMBED_AU_URI,XSP_AU);
	}

	private ComponentModule module;
	private Map config = new HashMap();
	private Servlet servlet;
	private DHtmlLayoutServlet layoutServlet;
	private DHtmlUpdateServlet auServlet;
	private boolean zk_initialed = false;
	

	public void init(ComponentModule module) {
		this.module = module;
	}
	
	public ServletMatch getServletMatch(String contextPath, String path)
			throws ServletException {
		if(path.startsWith(XSP_AU)){
			initialServelts();
			return new ServletMatch(servlet,AU,path.substring(XSP_AU.length()));
//			return new ServletMatch(module.createServlet(XspAuServlet.class, "xspAuServlet", config),AU,path.substring(XSP_AU.length()));
		}
		return null;
	}

	private void initialServelts() throws ServletException {
		if(servlet!=null) return;
		synchronized(this){
			if(servlet!=null) return;
			layoutServlet = new DHtmlLayoutServlet();
			auServlet = new DHtmlUpdateServlet();
			servlet = module.createServlet(new XspAuServlet(), "xspAuServlet", config);
		}
	}
	
	public class XspAuServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;
		public void init(ServletConfig config) throws ServletException {
			super.init(config);
			if(zk_initialed) return;
			synchronized(this){
				if(zk_initialed) return;
				config = new ServletConfigAdapter(config);
				layoutServlet.init(config);
				auServlet.init(config);
				zk_initialed = true;
			}
		}
		
		@Override
		public void destroy() {
			super.destroy();
			synchronized(this){
				if(layoutServlet!=null){
					layoutServlet.destroy();
					auServlet.destroy();
					layoutServlet = null;
					auServlet = null;
				}
			}
		}

		public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
			doGet(req,res);
		}
		
		public void doGet(HttpServletRequest req, HttpServletResponse res)
				throws ServletException, IOException {
			
			
			
			
			
			try{
//				req.getRequestDispatcher("/zkau").forward(req,res);
//				req.getRequestDispatcher("/zkau").include(req,res);
				auServlet.service(req,res);
			}catch(ServletException x){
				log.error(x.getMessage(),x);
				throw x;
			}catch(IOException x){
				log.error(x.getMessage(),x);
				throw x;
			}catch(Exception x){
				x.printStackTrace();
				log.error(x.getMessage(),x);
				throw new ServletException(x.getMessage(),x);
			}
			
			
			
//			PrintWriter out = res.getWriter();
//			String username = req.getParameter("username");
//			String password = req.getParameter("password");
//			if (username == null || password == null) {
//				out.println("Miss parameter username or password!");
//			} else {
//				if (username.trim().equals("admin")
//						&& password.trim().equals("admin")) {
//					out.println("Login Success");
//				} else
//					out.println("Login Failure");
//			}
//			out.println(":"+req.getPathInfo());
//			out.close();
		}
	}
	
	

	public class ServletConfigAdapter implements ServletConfig{
		
		protected ServletConfig config;
		protected boolean compress = false;
		
		public ServletConfigAdapter(ServletConfig config){
			this.config = config;
		}
		
		public String getInitParameter(String parm) {
			if("update-uri".equals(parm)){
				return XSP_AU;
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


}
