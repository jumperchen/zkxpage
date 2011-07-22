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

import java.util.HashMap;

import javax.faces.context.FacesContext;

import org.zkoss.xpage.core.component.ZulComponentBase;
/**
 * a request scope jsf helper context 
 * @author Dennis Chen
 *
 */
@SuppressWarnings("unchecked")
public class ComponentBinding extends HashMap{

    private static final long serialVersionUID = 1L;

	public static ComponentBinding instance(){
		FacesContext fc = FacesContext.getCurrentInstance();
		if(fc==null){
			//not in faces scope.
			return new ComponentBinding();
		}
		
		ComponentBinding binding = (ComponentBinding)fc.getApplication().createValueBinding("#{zkComponentBinding}").getValue(fc);
		return binding;
	}
	
	public static ZulComponentBase getComponent(String name){
		return ((ZulComponentBase)ComponentBinding.instance().get(name));
	}
}
