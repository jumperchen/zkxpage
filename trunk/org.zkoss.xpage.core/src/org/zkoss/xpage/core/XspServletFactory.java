/*
 * Copyright 2011 Potix Corporation. All Rights Reserved.
 * 
 * Licensed under the GNU GENERAL PUBLIC LICENSE Version 3 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.gnu.org/licenses/gpl-3.0.txt
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.zkoss.xpage.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.lang.Library;
import org.zkoss.xpage.core.util.Log;
import org.zkoss.xpage.core.web.AuServlet;
import org.zkoss.xpage.core.web.LayoutServlet;

import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.IServletFactory;
import com.ibm.designer.runtime.domino.adapter.ServletMatch;
/**
 * handle zk servlets in xsp context
 * @author Dennis Chen
 *
 */
public class XspServletFactory implements IServletFactory {


	/** see org.zkoss.zkplus.embed.Renders **/
	static final String EMBED_AU_URI = "org.zkoss.zkplus.embed.updateURI";
	static final String XSP_AU = "/xsp/zkau";
	static final String AU = "/zkau";
	
	//in xpage (8.5.2), compress header is not work, so always disable
	static final Boolean COMPRESS= Boolean.FALSE;
	
	static{
		Library.setProperty(EMBED_AU_URI,XSP_AU);
	}

	private ComponentModule module;
	@SuppressWarnings("unchecked")
	private Map config = new HashMap();
	private Servlet servlet;
	private LayoutServlet layoutServlet;
	private AuServlet auServlet;
	private boolean zk_initialed = false;
	

	@SuppressWarnings("unchecked")
	public void init(ComponentModule module) {
		this.module = module;
		Log.log(this,"init:"+module.getModuleName()+":in "+this);
		config.put("update-uri", XSP_AU);
		//disable compress, domino doesn't allow it.
		config.put("compress", COMPRESS.toString());
		
	}
	
	public ServletMatch getServletMatch(String contextPath, String path)
			throws ServletException {
		if(path.startsWith(XSP_AU)){
			initialServelts();
			ServletMatch sm = new ServletMatch(servlet,AU,path.substring(XSP_AU.length()));
			return sm;
		}
		return null;
	}

	/** initial zk servlets, NOTE no way to destroy for now **/
	private void initialServelts() throws ServletException {
		if(servlet!=null) return;
		synchronized(this){
			if(servlet!=null) return;
			layoutServlet = new LayoutServlet();
			auServlet = new AuServlet();
			servlet = module.createServlet(new XspAuServlet(), "xspAuServlet", config);
		}
	}
	
	/** 
	 * a servlet for xsp ServletMatch, this servlet will be created every request,
	 * however we always use same zk servlet instance to service the request. 
	 */
	public class XspAuServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;
		public void init(ServletConfig config) throws ServletException {
			//inital will be called every request , so I need to control it per ServletFactory scope.
			super.init(config);
			if(zk_initialed) return;
			synchronized(this){
				if(zk_initialed) return;
				layoutServlet.init(config);
				auServlet.init(config);
				zk_initialed = true;
			}
		}
		
		@Override
		public void destroy() {
			//never called in xsp 
			super.destroy();
			
			//we should not destory sicne this servlet is created every request
//			synchronized(this){
//				if(layoutServlet!=null){
//					layoutServlet.destroy();
//					auServlet.destroy();
//					layoutServlet = null;
//					auServlet = null;
//					servlet = null;
//				}
//			}
		}

		public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
			doGet(req,res);
		}
		
		public void doGet(HttpServletRequest req, HttpServletResponse res)
				throws ServletException, IOException {
			try{
				//not work
//				req.getRequestDispatcher("/zkau").forward(req,res);
//				req.getRequestDispatcher("/zkau").include(req,res);

				//work
				auServlet.service(req,res);
				
				//I have to flush my self, or something the response will not flush out
				res.flushBuffer();
			}catch(ServletException x){
				Log.error(this,x.getMessage(),x);
				throw x;
			}catch(IOException x){
				Log.error(this,x.getMessage(),x);
				throw x;
			}catch(Exception x){
				x.printStackTrace();
				Log.error(this,x.getMessage(),x);
				throw new ServletException(x.getMessage(),x);
			}
		}
	}
}
