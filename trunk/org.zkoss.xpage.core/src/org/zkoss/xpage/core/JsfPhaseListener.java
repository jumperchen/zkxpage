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
