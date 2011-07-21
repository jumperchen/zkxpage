package org.zkoss.xpage.core;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

public class JsfPhaseListener implements javax.faces.event.PhaseListener{
	private static final long serialVersionUID = 1L;
	
	public void afterPhase(PhaseEvent event) {
		PhaseId pi = event.getPhaseId();
		if(PhaseId.RESTORE_VIEW.equals(pi)){
			//mark postback
			FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(Constants.POSTBACK_KEY, "");
		}
	}

	public void beforePhase(PhaseEvent event) {	
	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
