package org.zkoss.xpage.zss.library;

import com.ibm.xsp.library.AbstractXspLibrary;

public class XspLibrary extends AbstractXspLibrary {

	public String getLibraryId() {
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
		return "0.5.0";
	}

}
