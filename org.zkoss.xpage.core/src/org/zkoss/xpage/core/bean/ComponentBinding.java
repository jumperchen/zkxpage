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
