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
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.domino.xsp.module.nsf.NotesContext;


public class ExampleServlet extends HttpServlet{

	private static final long serialVersionUID = 3492056914599940177L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	private static String getUserName(){
		try{
			NotesContext nc = NotesContext.getCurrentUnchecked();
			if(nc==null) return "no context";
			Object sess = nc.getCurrentSession();
			if(sess==null) return "no session";
			
			Method sm = sess.getClass().getMethod("getUserName");
            String username = (String)sm.invoke(sess, (Object[])null);
            return username;
		}catch(Throwable x){
			x.printStackTrace();
			return x.getMessage();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		new Exception("in servlet").printStackTrace();
		HttpSession hess = req.getSession();
		PrintWriter pw = resp.getWriter();
		
		pw.append("<br/>Hi "+getUserName());
		pw.append("<br/>this"+this+", session instance is "+hess+" , id "+(hess==null?"":hess.getId()));
		
		pw.flush();
	}

}
