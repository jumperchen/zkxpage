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
package org.zkoss.xpage.zss;

import com.ibm.xsp.library.AbstractXspLibrary;

/**
 * 
 * @author Dennis Chen
 *
 */
public class XspLibrary extends AbstractXspLibrary {

	public String getLibraryId() {
		//the id of this xsp component, will be related by application, so don't modify after release
		return "org.zkoss.xpage.zss.library";
	}

	@Override
	public String[] getFacesConfigFiles() {
		return new String[] { "META-INF/zk-xpage-zss-faces-config.xml" };
	}

	@Override
	public String getPluginId() {
		return "org.zkoss.xpage.zss";
	}

	@Override
	public String[] getXspConfigFiles() {
		return new String[] { "META-INF/zk-xpage-zss.xsp-config" };
	}

	@Override
	public String getTagVersion() {
		return "0.8.0";
	}

}
