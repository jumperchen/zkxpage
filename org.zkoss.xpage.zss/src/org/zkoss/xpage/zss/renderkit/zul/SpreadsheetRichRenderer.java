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
package org.zkoss.xpage.zss.renderkit.zul;

import javax.faces.context.FacesContext;

import org.zkoss.xpage.core.component.ZulBridgeBase;
import org.zkoss.xpage.core.renderkit.zul.ZulRendererBase;
import org.zkoss.xpage.zss.component.SpreadsheetRichBridge;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zss.app.zul.Zssapp;
import org.zkoss.zss.model.Book;

public class SpreadsheetRichRenderer extends ZulRendererBase {
	
	@Override
	protected HtmlBasedComponent createRootComponent(Page page,ZulBridgeBase bridge){
		Zssapp app = new Zssapp();
		return app;
	}
	
	@Override
	protected void applyAttributes(ZulBridgeBase bridge, HtmlBasedComponent comp) throws Exception {
		super.applyAttributes(bridge, comp);
		SpreadsheetRichBridge zss = (SpreadsheetRichBridge) bridge;
		Zssapp ss = (Zssapp) comp;

		Integer in = zss.getMaxrows();

		if (in != null) {
			ss.setMaxrows(in.intValue());
		}
		in = zss.getMaxcolumns();
		if (in != null) {
			ss.setMaxcolumns(in.intValue());
		}

		Book book = zss.getBook();
		if (book != null) {
			ss.setBook(book);
		} else {
			String src = zss.getSrc();
			book = BookLoader.loadBook(FacesContext.getCurrentInstance(), ss.getSpreadsheet().getImporter(), src);
			if (book != null) {
				ss.setBook(book);
			}
		}
	}
	
	@Override
	protected void afterComposer(final ZulBridgeBase bridge, final HtmlBasedComponent comp,final FacesContext context) throws Exception{
		Book book = ((Zssapp)comp).getSpreadsheet().getBook();
		if(book==null){
			((Zssapp)comp).setBook(BookLoader.loadDefaultBook());
		}
	}
}
