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
package org.zkoss.xpage.core.web;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.xpage.core.util.Log;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.http.WebManager;

/**
 * warp {@link DHtmlUpdateServlet}
 * @author Dennis Chen
 *
 */
public class AuServlet /* extends HttpServlet {// */extends DHtmlUpdateServlet {
	private static final long serialVersionUID = 1L;

	public void init(final ServletConfig config) throws ServletException {
		Log.log(this,"init::" + this+",context = "+config.getServletContext());
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(AuServlet.class.getClassLoader());
			Log.log(this,"init:: att1 = "+config.getServletContext().getAttribute("att1"));
			super.init(config);
		} catch (Exception x) {
			Log.error(this,x.getMessage(), x);
			throw new ServletException(x.getMessage(), x);
		}finally{
			Thread.currentThread().setContextClassLoader(cl);
		}
		Log.log(this,"init end::" + this);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log.log(this,"doGet::" + this+", servletPath "+request.getServletPath()+", pathInfo:"+request.getPathInfo());
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(AuServlet.class.getClassLoader());
			
			String dtid = request.getParameter("dtid");
			final Session sess = WebManager.getSession(getServletContext(), request, false);
			Desktop dt = getDesktop(sess,dtid);
			
			Log.log(this,"session="+request.getSession().getId()+", dtid="+dtid+",desktop = "+dt);
			super.doGet(request, response);
			
		} catch (ServletException x) {
			Log.error(this,x.getMessage(),x);
			throw x;
		} catch (IOException x) {
			Log.error(this,x.getMessage(),x);
			throw x;
		} catch (Exception x) {
			Log.error(this,x.getMessage(),x);
			throw new ServletException(x.getMessage(),x);
		}finally{
			Thread.currentThread().setContextClassLoader(cl);
		}
		Log.log(this,"doGet end::" + this);

	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doGet(req, resp);
	}
}
