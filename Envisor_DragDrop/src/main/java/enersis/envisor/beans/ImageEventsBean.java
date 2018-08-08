package enersis.envisor.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.extensions.event.RotateEvent;
import org.springframework.stereotype.Component;

/***
 * 
 * @author TatvaSoft
 * ImageEventsBean : JSF Backend Bean to manage Image property.
 * It is used to update and synchronize image properties (image rotation, resize, select)  page data and backend bean data.
 *
 */

@Component("imageEventsBean")
@ViewScoped
@ManagedBean
public class ImageEventsBean extends AbstractBacking implements Serializable {

	private static final long serialVersionUID = 4154486092805139422L;

	public void selectEndListener(final ImageAreaSelectEvent e) {
		final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Area selected", "X1: " + e.getX1() + ", X2: " + e.getX2()
						+ ", Y1: " + e.getY1() + ", Y2: " + e.getY2()
						+ ", Image width: " + e.getImgWidth()
						+ ", Image height: " + e.getImgHeight());

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void rotateListener(final RotateEvent e) {
		final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Image rotated", "Degree:" + e.getDegree());

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void resizeListener(final ResizeEvent e) {
		final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Image resized", "Width:" + e.getWidth() + ", Height: "
						+ e.getHeight());

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

}
