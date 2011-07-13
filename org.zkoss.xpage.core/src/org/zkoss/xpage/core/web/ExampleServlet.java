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
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.zkoss.util.logging.Log;

import com.ibm.domino.xsp.module.nsf.NotesContext;


public class ExampleServlet extends HttpServlet{
	private static final Log log = Log.lookup(ExampleServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	private static String getUserName(){
		try{
			NotesContext nc = NotesContext.getCurrent();
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
	
		HttpSession hess = req.getSession();
		PrintWriter pw = resp.getWriter();
		HttpSession sess = req.getSession();
		
		pw.append("<br/>Hi "+getUserName());
		pw.append("<br/>this"+this+", session instance is "+hess+" , id "+(hess==null?"":hess.getId()));
		
		
		String session_attr1 = (String)sess.getAttribute("session_attr1");
		pw.append("<br/>session_attr1="+session_attr1);
		req.getSession().setAttribute("session_attr1", "set by "+this+",date:"+new Date());
		
		pw.flush();
	}

}
