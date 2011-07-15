package org.zkoss.xpage.core.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.http.WebManager;
import org.zkoss.zk.ui.sys.WebAppCtrl;

public class DesktopUtil {

	
	public static Desktop findDesktop(ServletContext ctx,HttpServletRequest request,String dtid){
		//refer code form DHtmlUpateServlet
		Session sess = WebManager.getSession(ctx, request, false);
		Desktop desktop = null;
		if(sess!=null){
			desktop = ((WebAppCtrl)sess.getWebApp()).getDesktopCache(sess).getDesktopIfAny(dtid);
		}
		return desktop;
	}
	
	public static Component findComponent(Desktop desktop,String uuid){
		return desktop.getComponentByUuid(uuid);
	}
	
}
