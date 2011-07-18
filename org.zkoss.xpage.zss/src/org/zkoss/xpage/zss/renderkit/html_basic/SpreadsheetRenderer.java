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

import java.io.IOException;
import java.io.InputStream;

import org.zkoss.xpage.core.component.ZKComponentBase;
import org.zkoss.xpage.core.renderkit.html_basic.ZKRendererBase;
import org.zkoss.xpage.core.util.Log;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Importers;
import org.zkoss.zss.model.impl.ExcelImporter;
import org.zkoss.zss.ui.Spreadsheet;

public class SpreadsheetRenderer extends ZKRendererBase {
	
	@Override
	protected Component createZKComponent(Page page,ZKComponentBase zcomp){
		Spreadsheet spreadsheet = new Spreadsheet();
		spreadsheet.setPage(page);
		spreadsheet.setWidth("600px");
		spreadsheet.setHeight("300px");
		
		spreadsheet.setPage(page);
		spreadsheet.setWidth("600px");
		spreadsheet.setHeight("300px");
		
		spreadsheet.setMaxcolumns(40);
		spreadsheet.setMaxrows(40);
		
		final ExcelImporter importer = (ExcelImporter)Importers.getImporter("excel");
		InputStream is = getClass().getResourceAsStream("empty.xls");
		try{
			Book book = importer.imports(is, "empty");
			spreadsheet.setBook(book);
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
		
		
//		applyProperties();
//		spreadsheet.setId(getClientId());
//		doAfterCompose();
		return spreadsheet;
	}
}
