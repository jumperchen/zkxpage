package org.zkoss.xpage.core.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.zkoss.util.logging.Log;


public class ExampleServlet extends HttpServlet{
	private static final Log log = Log.lookup(ExampleServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		HttpSession hess = req.getSession();
		PrintWriter pw = resp.getWriter();
		
		String session_attr1 = (String)req.getSession().getAttribute("session_attr1");
		pw.append("session_attr1="+session_attr1);
		req.getSession().setAttribute("session_attr1", "set by "+this+",date:"+new Date());
		
		pw.append("<br/>THIS IS SERVLET AA "+this+", session instance is "+hess+" , id "+(hess==null?"":hess.getId()));
		pw.flush();
	}

}
