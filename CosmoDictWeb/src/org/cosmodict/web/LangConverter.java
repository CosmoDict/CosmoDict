package org.cosmodict.web;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.cosmodict.jpa.Lang;

@FacesConverter("org.cosmodict.web.LangConverter")
public class LangConverter implements Converter {

	public LangConverter() {
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return Manager.langsMap.get(arg2);
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		Lang lang = (Lang) arg2;
		return (lang != null) ? lang.getLangId() : null;
	}

}
