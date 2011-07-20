package org.zkoss.xpage.core.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.zkoss.xpage.core.Constants;

public class AuContext {

	List auScripts = new ArrayList();

	public static AuContext instance() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if (fc == null) {
			// not in faces scope.
			return new AuContext();
		}
		Map reqMap = fc.getExternalContext().getRequestMap();

		AuContext context = (AuContext) reqMap.get(Constants.AUCONTEXT_KEY);
		if (context == null) {
			context = new AuContext();
			reqMap.put(Constants.AUCONTEXT_KEY, context);
		}
		return context;
	}

	public void addScript(String script) {
		auScripts.add(script);
	}

	public Iterator iteratorScripts() {
		return auScripts.iterator();
	}

	public void clearScripts() {
		auScripts.clear();
	}

	public boolean hasScript() {
		return auScripts.size()>0;
	}

}
