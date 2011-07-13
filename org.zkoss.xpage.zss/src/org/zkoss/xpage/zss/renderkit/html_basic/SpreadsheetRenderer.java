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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;
import org.zkoss.zkplus.embed.Renders;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Importers;
import org.zkoss.zss.model.impl.ExcelImporter;
import org.zkoss.zss.ui.Spreadsheet;

public class SpreadsheetRenderer extends javax.faces.render.Renderer {

	private static final Log log = Log.lookup(SpreadsheetRenderer.class);

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		final HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		final HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		// ZK component
		ServletContext svlctx = (ServletContext) context.getExternalContext()
				.getContext();
		try {
			Renders.render(svlctx, request, response, new GenericRichlet() {
				public void service(Page page) throws Exception {

//					Button button = new Button();
//					button.setPage(page);
//					button.setLabel("Spreadsheet");
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
						log.error(x.getMessage(),x);
						throw x;
					}finally{
						if(is!=null){
							is.close();
						}
					}					
					
					
//					applyProperties();
//					spreadsheet.setId(getClientId());
//					doAfterCompose();
				}
			}, null, writer);
		} catch (ServletException e) {
			log.error(e.getMessage());
			throw new IOException(e.getMessage());
		}
	}
}
