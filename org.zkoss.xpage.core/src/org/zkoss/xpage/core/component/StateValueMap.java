package org.zkoss.xpage.core.component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class StateValueMap implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	Map values;

	@SuppressWarnings("unchecked")
	public void set(String key,Object value){
		if(values==null){
			values = new HashMap();
		}
		if(value==null){
			values.remove(key);
		}else{
			values.put(key, value);
		}
	}
	
	public boolean contains(String key){
		return values==null?false:values.containsKey(key);
	}
	
	public Object get(String key){
		return values==null?null:values.get(key);
	}
	
	public String getString(String key){
		return (String)get(key);
	}
	
	public String getString(String name,FacesContext context, UIComponent comp){
		if(contains(name)){
			return getString(name);
		}
		ValueBinding vb = comp.getValueBinding(name);
		if(vb!=null){
			return (String)vb.getValue(context);
		}
		return null;
	}
	
	
	public Integer getInteger(String key){
		return (Integer)get(key);
	}
	public Integer getInteger(String name,FacesContext context, UIComponent comp){
		if(contains(name)){
			return getInteger(name);
		}
		ValueBinding vb = comp.getValueBinding(name);
		if(vb!=null){
			return (Integer)vb.getValue(context);
		}
		return null;
	}
	
	
	public Long getLong(String key){
		return (Long)get(key);
	}
	public Long getLong(String name,FacesContext context, UIComponent comp){
		if(contains(name)){
			return getLong(name);
		}
		ValueBinding vb = comp.getValueBinding(name);
		if(vb!=null){
			return (Long)vb.getValue(context);
		}
		return null;
	}
	
	public Double getDouble(String key){
		return (Double)get(key);
	}
	public Double getDouble(String name,FacesContext context, UIComponent comp){
		if(contains(name)){
			return getDouble(name);
		}
		ValueBinding vb = comp.getValueBinding(name);
		if(vb!=null){
			return (Double)vb.getValue(context);
		}
		return null;
	}
	
	public Float getFloat(String key){
		return (Float)get(key);
	}
	public Float getFloat(String name,FacesContext context, UIComponent comp){
		if(contains(name)){
			return getFloat(name);
		}
		ValueBinding vb = comp.getValueBinding(name);
		if(vb!=null){
			return (Float)vb.getValue(context);
		}
		return null;
	}

	public Boolean getBoolean(String key){
		return (Boolean)get(key);
	}
	public Boolean getBoolean(String name,FacesContext context, UIComponent comp){
		if(contains(name)){
			return getBoolean(name);
		}
		ValueBinding vb = comp.getValueBinding(name);
		if(vb!=null){
			return (Boolean)vb.getValue(context);
		}
		return null;
	}
}
