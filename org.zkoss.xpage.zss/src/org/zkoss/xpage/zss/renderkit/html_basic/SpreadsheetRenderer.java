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
package org.zkoss.xpage.zss.renderkit.html_basic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.context.FacesContext;

import org.zkoss.lang.Strings;
import org.zkoss.util.resource.ClassLocator;
import org.zkoss.xpage.core.component.ZulBridgeBase;
import org.zkoss.xpage.core.renderkit.html_basic.ZulRendererBase;
import org.zkoss.xpage.core.util.Log;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Importer;
import org.zkoss.zss.model.impl.ExcelImporter;
import org.zkoss.zss.ui.Spreadsheet;

public class SpreadsheetRenderer extends ZulRendererBase {
	
	@Override
	protected HtmlBasedComponent createRootComponent(Page page,ZulBridgeBase bridge){
		Spreadsheet spreadsheet = new Spreadsheet();
		return spreadsheet;
	}
	
	@Override
	protected void applyAttributes(ZulBridgeBase bridge, HtmlBasedComponent comp) throws Exception {
		super.applyAttributes(bridge, comp);
		if(comp instanceof Spreadsheet){
			org.zkoss.xpage.zss.component.SpreadsheetBridge zss = (org.zkoss.xpage.zss.component.SpreadsheetBridge)bridge;
			Spreadsheet ss = (Spreadsheet)comp;
			
			Integer in = zss.getMaxrows();

			if(in!=null){
				ss.setMaxrows(in.intValue());
			}
			in = zss.getMaxcolumns();
			if(in!=null){
				ss.setMaxcolumns(in.intValue());
			}
			
			
			Book book = zss.getBook();
			if(book!=null){
				ss.setBook(book);
			}else{
				String src = zss.getSrc();
				book = loadBook(FacesContext.getCurrentInstance(),ss.getImporter(),src);
				if(book!=null){
					ss.setBook(book);
				}
			}

		}
	}
	
	@Override
	protected void afterComposer(final ZulBridgeBase bridge, final HtmlBasedComponent comp) throws Exception{
		Book book = ((Spreadsheet)comp).getBook();
		if(book==null){
			((Spreadsheet)comp).setBook(loadDefaultBook());
		}
	}
	
	/*
	 * the zk implementation cannot load resource form web content, so we use jsfcontext to load web context resource here 
	 */
	private Book loadBook(FacesContext context, Importer importer, String src) throws MalformedURLException {
		if (Strings.isBlank(src)) {
			return null;
		}

		if (importer == null) {
			importer = new ExcelImporter();
		}

		Book book = null;
		if (importer instanceof ExcelImporter) {
			URL url = null;

			if (src.startsWith("/")) {// try to load by application
				// context.
				url = context.getExternalContext().getResource(src);
			}
			if (url == null) {// try to load from class loader
				url = new ClassLocator().getResource(src);
			}
			if (url == null) {// try to load from file
				File f = new File(src);
				if (f.exists()) {
					url = f.toURI().toURL();
				}
			}

			if (url == null) {
				throw new UiException("resource for " + src + " not found.");
			}

			book = ((ExcelImporter) importer).importsFromURL(url);
		} else {
			book = importer.imports(src);
		}
		return book;
	}

	/* load default empty book */
	private Book loadDefaultBook() {
		final ExcelImporter importer = new ExcelImporter();
		//TODO read from some configuration
		InputStream is = getClass().getResourceAsStream("empty.xls");
		try{
			Book book = importer.imports(is, "default");
			return book;
		}catch(Exception x){
			Log.error(this,x.getMessage(),x);
			throw new RuntimeException(x);
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException x) {
					throw new RuntimeException(x);
				}
			}
		}	
	}
}
