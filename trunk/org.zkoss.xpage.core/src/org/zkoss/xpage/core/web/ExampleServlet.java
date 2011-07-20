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
		HttpSession sess = req.getSession();
		
		pw.append("<br/>Hi "+getUserName());
		pw.append("<br/>this"+this+", session instance is "+hess+" , id "+(hess==null?"":hess.getId()));
		
		pw.flush();
	}

}