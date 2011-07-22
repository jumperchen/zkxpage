/* 
{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Jul 22, 2011 , Created by Dennis Chen
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.xpage.core.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.http.WebManager;
import org.zkoss.zk.ui.sys.WebAppCtrl;
/**
 * zk util
 * @author Dennis Chen
 *
 */
public class ZkUtil {

	/**
	 * find desktop by desktop id(dtid)
	 * @return null if not found
	 */
	public static Desktop findDesktop(ServletContext ctx,HttpServletRequest request,String dtid){
		//refer code form DHtmlUpateServlet
		Session sess = WebManager.getSession(ctx, request, false);
		Desktop desktop = null;
		if(sess!=null){
			desktop = ((WebAppCtrl)sess.getWebApp()).getDesktopCache(sess).getDesktopIfAny(dtid);
		}
		return desktop;
	}
	
	/**
	 * find component in desktop by uuid, the uuid should come from {@link Component#getUuid()}
	 */
	public static Component findComponent(Desktop desktop,String uuid){
		return desktop.getComponentByUuid(uuid);
	}
	
}
