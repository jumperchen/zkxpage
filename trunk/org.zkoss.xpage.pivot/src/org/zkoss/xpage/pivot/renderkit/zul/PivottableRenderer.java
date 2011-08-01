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
package org.zkoss.xpage.pivot.renderkit.zul;

import javax.faces.context.FacesContext;

import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.PivotModel;
import org.zkoss.pivot.Pivottable;
import org.zkoss.xpage.core.component.ZulBridgeBase;
import org.zkoss.xpage.core.renderkit.zul.ZulRendererBase;
import org.zkoss.xpage.pivot.component.PivottableBridge;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;

public class PivottableRenderer extends ZulRendererBase {

	@Override
	protected HtmlBasedComponent createRootComponent(Page page, ZulBridgeBase zbridge) {
		Pivottable comp = new Pivottable();
		return comp;
	}

	@Override
	protected void applyAttributes(ZulBridgeBase bridge, HtmlBasedComponent comp) throws Exception {
		super.applyAttributes(bridge, comp);
		PivottableBridge pivb = (PivottableBridge) bridge;
		Pivottable piv = (Pivottable) comp;

		String str = pivb.getDataFieldOrient();
		if(str!=null){
			piv.setDataFieldOrient(str);
		}
		
		Integer in = pivb.getPageSize();
		if (in != null) {
			piv.setPageSize(in.intValue());
		}

		Boolean bo = pivb.getGrandTotalForColumns();
		if (bo != null) {
			piv.setGrandTotalForColumns(bo.booleanValue());
		}

		bo = pivb.getGrandTotalForRows();
		if (bo != null) {
			piv.setGrandTotalForRows(bo.booleanValue());
		}

		PivotModel model = pivb.getModel();

		if (model != null) {
			piv.setModel(model);
		}

	}

	@Override
	protected void afterComposer(final ZulBridgeBase bridge, final HtmlBasedComponent comp, final FacesContext context)
	        throws Exception {
		Pivottable piv = (Pivottable) comp;

		// double check, for friendly message
		PivotModel model = piv.getModel();
		if (model == null) {
			throw new NullPointerException("PivotModel is null");
		}
		PivotField pfs[] = model.getDataFields();
		if (pfs == null || pfs.length == 0) {
			throw new IllegalStateException("Must have aleast one data field in model");
		}

	}
}
