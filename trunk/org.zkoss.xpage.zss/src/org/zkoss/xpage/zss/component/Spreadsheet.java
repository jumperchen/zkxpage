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
package org.zkoss.xpage.zss.component;

import javax.faces.el.ValueBinding;

import org.zkoss.xpage.core.Constants;
import org.zkoss.xpage.core.component.ZulComponentBase;
import org.zkoss.zss.model.Book;
/**
 * zk spreadsheet jsf component in Domino.
 * @author Dennis Chen
 *
 */
public class Spreadsheet extends ZulComponentBase {

	private static final long serialVersionUID = 1L;

	public Spreadsheet() {
		super();
		setRendererType("org.zkoss.xpage.spreadsheet");
	}

	@Override
	public String getFamily() {
		return Constants.COMPONENT_FAMILY;
	}

	
	public Integer getMaxrows() {
		return states.getInteger("maxrows",getFacesContext(),this);
	}
	/**
	 * the maximum row number of this spread sheet.
	 */
	public void setMaxrows(Integer maxrows) {
		states.set("maxrows", maxrows);
	}

	
	public Integer getMaxcolumns() {
		return states.getInteger("maxcolumns",getFacesContext(),this);
	}
	/**
	 * the maximum column number of this spread sheet.
	 */
	public void setMaxcolumns(Integer maxcolumns) {
		states.set("maxcolumns", maxcolumns);
	}


	public String getSrc() {
		return states.getString("src",getFacesContext(),this);
	}

	/**
	 * the src location of book model.
	 */
	public void setSrc(String src) {
		states.set("src", src);
	}
	
	
	public Book getBook() {
		ValueBinding vb = getValueBinding("book");
		if(vb!=null){
			return (Book)vb.getValue(getFacesContext());
		}
		return null;
	}
	
	

}
