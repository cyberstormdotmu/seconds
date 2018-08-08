package enersis.envisor.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.DragDropEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("dragDropBean")
//@Scope("view")
@ViewScoped
// @SessionScoped
// @RequestScoped
@ManagedBean
public class DragDropBean extends AbstractBacking implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1696864251674031514L;

	public void onDrop(DragDropEvent event)
	{
//		event.
	}
	
}
