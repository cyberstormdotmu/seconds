package enersis.envisor.beans;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import enersis.envisor.entity.Wall;
import enersis.envisor.service.WallService;

@FacesConverter("wallConverter")
public class WallConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String value) {
		if(value != null && value.trim().length() > 0) {
            try {
                WallService service = (WallService) context.getExternalContext().getApplicationMap().get("themeService");
                return service.findById(Integer.parseInt(value));
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Wall."));
            }
        }
        else {
            return null;
        }	
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,Object object) {
		if(object != null) {
            return String.valueOf(((Wall) object).getId());
        }
        else {
            return null;
        }	
	}

	
}
