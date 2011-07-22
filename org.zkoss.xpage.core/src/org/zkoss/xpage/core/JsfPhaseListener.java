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
package org.zkoss.xpage.core;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

/**
 * 
 * @author Dennis Chen
 *
 */
public class JsfPhaseListener implements javax.faces.event.PhaseListener{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public void afterPhase(PhaseEvent event) {
		PhaseId pi = event.getPhaseId();
		if(PhaseId.RESTORE_VIEW.equals(pi)){
			//mark this request is a postback
			FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(Constants.POSTBACK_KEY, "");
		}
	}

	public void beforePhase(PhaseEvent event) {	
	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
