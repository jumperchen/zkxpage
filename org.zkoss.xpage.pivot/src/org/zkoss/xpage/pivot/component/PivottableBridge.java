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
package org.zkoss.xpage.pivot.component;

import javax.faces.el.ValueBinding;

import org.zkoss.pivot.PivotModel;
import org.zkoss.pivot.Pivottable;
import org.zkoss.xpage.core.Constants;
import org.zkoss.xpage.core.component.ZulBridgeBase;
import org.zkoss.zk.ui.Component;

/**
 * zk pivottable jsf component in Domino.
 * @author Dennis Chen
 *
 */
public class PivottableBridge extends ZulBridgeBase {

	private static final long serialVersionUID = 1L;
	

	public PivottableBridge() {
		super();
		setRendererType("org.zkoss.xpage.pivottable");
	}

	@Override
	public String getFamily() {
		return Constants.COMPONENT_FAMILY;
	}


	public Boolean getGrandTotalForColumns() {
		return states.getBoolean("grandTotalForColumns",getFacesContext(),this);
    }

	public void setGrandTotalForColumns(Boolean grandTotalForColumns) {
		states.set("grandTotalForColumns", grandTotalForColumns);
    }

	public Boolean getGrandTotalForRows() {
		return states.getBoolean("grandTotalForRows",getFacesContext(),this);
    }

	public void setGrandTotalForRows(Boolean grandTotalForRows) {
		states.set("grandTotalForRows", grandTotalForRows);
    }

	public Integer getPageSize() {
		return states.getInteger("pageSize",getFacesContext(),this);
    }

	public void setPageSize(Integer pageSize) {
		states.set("pageSize", pageSize);
    }

	public PivotModel getModel() {
		return (PivotModel)states.get("model",getFacesContext(),this);
    }

	public void setModel(Object model) {
		if(model instanceof ValueBinding){
			this.setValueBinding("model", (ValueBinding)model);
		}
		throw new UnsupportedOperationException("Please use ValueBinding to set the model");
    }


	public Pivottable getPivottable(){
		Component comp = getComponent();
		if(comp instanceof Pivottable){
			return (Pivottable)comp;
		}
		return null;
	}
	
	
	
}
