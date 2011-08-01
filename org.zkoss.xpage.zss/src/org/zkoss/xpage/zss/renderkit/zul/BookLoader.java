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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.context.FacesContext;

import org.zkoss.lang.Strings;
import org.zkoss.util.resource.ClassLocator;
import org.zkoss.xpage.core.util.Log;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Importer;
import org.zkoss.zss.model.impl.ExcelImporter;

class BookLoader {
	/*
	 * the zk implementation cannot load resource form web content, so we use jsfcontext to load web context resource here 
	 */
	static Book loadBook(FacesContext context, Importer importer, String src) throws MalformedURLException {
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
	static Book loadDefaultBook() {
		final ExcelImporter importer = new ExcelImporter();
		//TODO read from some configuration
		InputStream is = BookLoader.class.getResourceAsStream("empty.xls");
		try{
			Book book = importer.imports(is, "default");
			return book;
		}catch(Exception x){
			Log.error(BookLoader.class,x.getMessage(),x);
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
