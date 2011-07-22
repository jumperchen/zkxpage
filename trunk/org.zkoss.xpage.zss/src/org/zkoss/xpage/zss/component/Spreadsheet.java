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
