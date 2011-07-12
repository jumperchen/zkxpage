package org.zkoss.xpage.core.library;

import com.ibm.xsp.library.AbstractXspLibrary;

public class XspLibrary extends AbstractXspLibrary {

	public String getLibraryId() {
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
		return "0.5.0";
	}

	
	
}
