/* 
{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Jul 22, 2011 , Created by Dennis Chen
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.xpage.core.bean;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.zkoss.xpage.core.Constants;

import com.ibm.xsp.context.FacesContextEx;
/**
 * a request scope jsf helper context 
 * @author Dennis Chen
 *
 */
public class JsfContext {

	@SuppressWarnings("unchecked")
	public static JsfContext instance(){
		FacesContext fc = FacesContext.getCurrentInstance();
		if(fc==null){
			//not in faces scope.
			return new JsfContext();
		}
		JsfContext context = (JsfContext)fc.getApplication().createValueBinding("#{zkJsfContext}").getValue(fc);
		return context;
	}
	private FacesContext check(){
		FacesContextEx fc =  (FacesContextEx)FacesContext.getCurrentInstance();
		if(fc==null){
			throw new IllegalStateException("FacesContext not found");
		}
		return fc;
	}
	
	/**
	 * is current request a postback request
	 */
	public boolean isPostback(){
		FacesContext fc = check();
		if(fc.getExternalContext().getRequestMap().get(Constants.POSTBACK_KEY)!=null){
			return true;
		}
		return false;
	}
	
	/**
	 * is current request a partial refresh request
	 */
	public boolean isAjaxPartialRefresh(){
		FacesContextEx fc =  (FacesContextEx)check();
		
		boolean r = fc.isAjaxPartialRefresh();
		return r;
	}
	
	/**
	 * is comp in the partial refresh list (include descendant).
	 */
	public boolean isAjaxRendered(UIComponent comp){
		FacesContextEx fc =  (FacesContextEx)check();
		boolean r = fc.isAjaxRendered(comp);
		while(!r){
			comp = comp.getParent();
			if(comp!=null){
				r = fc.isAjaxRendered(comp);
			}else{
				break;
			}
		}
		return r;
	}
	
}
