/* 
{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		July 13, 2011 , Created by dennischen
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
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
import org.zkoss.xpage.core.component.ZulComponentBase;
import org.zkoss.xpage.core.renderkit.html_basic.ZulRendererBase;
import org.zkoss.xpage.core.util.Log;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Importer;
import org.zkoss.zss.model.impl.ExcelImporter;
import org.zkoss.zss.ui.Spreadsheet;

public class SpreadsheetRenderer extends ZulRendererBase {
	
	@Override
	protected Component createZKComponent(Page page,ZulComponentBase zcomp){
		Spreadsheet spreadsheet = new Spreadsheet();
		return spreadsheet;
	}
	
	@Override
	protected void applyAttributes(ZulComponentBase zcomp, Component comp) throws Exception {
		super.applyAttributes(zcomp, comp);
		if(comp instanceof Spreadsheet){
			org.zkoss.xpage.zss.component.Spreadsheet zss = (org.zkoss.xpage.zss.component.Spreadsheet)zcomp;
			Spreadsheet ss = (Spreadsheet)comp;
			
			Integer in = zss.getMaxrows();

			if(in!=null){
				ss.setMaxrows(in.intValue());
			}
			in = zss.getMaxcolumns();
			if(in!=null){
				ss.setMaxcolumns(in.intValue());
			}
			
			String src = zss.getSrc();
			Book book = zss.getBook();
			
			if(book!=null){
				ss.setBook(book);
			}else{
				book = loadBook(FacesContext.getCurrentInstance(),ss.getImporter(),src);
				ss.setBook(book);
			}

		}
	}
	
	/*
	 * the zk implementation cannot load resource form web content, so we use jsfcontext to load web context resource here 
	 */
	private Book loadBook(FacesContext context, Importer importer, String src) throws MalformedURLException {
		if (Strings.isBlank(src)) {
			return loadDefaultBook();
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
