package org.zkoss.xpage.core.bean;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.zkoss.xpage.core.Constants;

import com.ibm.xsp.context.FacesContextEx;

public class JsfContext {

	@SuppressWarnings("unchecked")
	public static JsfContext instance(){
		FacesContext fc = FacesContext.getCurrentInstance();
		if(fc==null){
			//not in faces scope.
			return new JsfContext();
		}
		Map reqMap = fc.getExternalContext().getRequestMap();
		
		JsfContext context = (JsfContext)reqMap.get(Constants.JSFCONTEXT_KEY);
		if(context==null){
			context = new JsfContext();
			reqMap.put(Constants.JSFCONTEXT_KEY, context);
		}
		return context;
	}
	private FacesContext check(){
		FacesContextEx fc =  (FacesContextEx)FacesContext.getCurrentInstance();
		if(fc==null){
			throw new IllegalStateException("FacesContext not found");
		}
		return fc;
	}
	
	public boolean isPostback(){
		FacesContext fc = check();
		if(fc.getExternalContext().getRequestMap().get(Constants.POSTBACK_KEY)!=null){
			return true;
		}
		return false;
	}
	
	public boolean isAjaxPartialRefresh(){
		FacesContextEx fc =  (FacesContextEx)check();
		
		boolean r = fc.isAjaxPartialRefresh();
		return r;
	}
	
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
