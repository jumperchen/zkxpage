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
package org.zkoss.xpage.core;

import com.ibm.xsp.library.AbstractXspLibrary;
/**
 * 
 * @author Dennis Chen
 *
 */
public class XspLibrary extends AbstractXspLibrary {

	public String getLibraryId() {
		//the id of this xsp component, will be related by application, so don't modify after release
		return "org.zkoss.xpage.core.library";
	}

	@Override
	public String[] getFacesConfigFiles() {
		return new String[] { "META-INF/zk-xpage-core-faces-config.xml"};
	}

	@Override
	public String getPluginId() {
		return "org.zkoss.xpage.core";
	}

	@Override
	public String[] getXspConfigFiles() {
		return new String[] { "META-INF/zk-xpage-core.xsp-config"};
	}

	@Override
	public String getTagVersion() {
		return "0.8.0";
	}

	
	
}
