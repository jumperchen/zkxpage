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
