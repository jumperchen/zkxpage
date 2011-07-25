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
