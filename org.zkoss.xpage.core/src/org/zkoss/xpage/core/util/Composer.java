package org.zkoss.xpage.core.util;

import javax.faces.context.FacesContext;

import org.zkoss.xpage.core.component.ZulBridgeBase;

public interface Composer {
	public void doAfterCompose(ZulBridgeBase bridge,FacesContext context) throws Exception;
}
