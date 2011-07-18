package org.zkoss.xpage.core.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

public class AuResult {

	private static final String REQ_KEY = "zk."+AuResult.class.getName();  
	
	
	List auScripts = new ArrayList();
	
	public static AuResult instance(){
		FacesContext fc = FacesContext.getCurrentInstance();
		if(fc==null){
			//not in faces scope.
			return new AuResult();
		}
		Map reqMap = fc.getExternalContext().getRequestMap();
		
		AuResult ar = (AuResult)reqMap.get(REQ_KEY);
		if(ar==null){
			ar = new AuResult();
			reqMap.put(REQ_KEY, ar);
		}
		return ar;
	}
	
	
	public void addScript(String script){
		auScripts.add(script);
	}
	
	public Iterator iteratorScripts(){
		return auScripts.iterator();
	}
	
	public void clearScripts(){
		auScripts.clear();
	}
	
	
}
