package enersis.envisor.beans;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;



//@Scope("view")
public abstract class AbstractBacking implements Serializable {

	private static final long serialVersionUID = 1L;

//	public User getCurrentUser() {
//		User userSession = (User) getSession().getAttribute(UserConstants.CURRENT_USER);
//		return userSession;
//	}
//
//	public void setCurrentUser(User currentUser) {
//		getSession().removeAttribute(UserConstants.CURRENT_USER);
//		if (null != currentUser) {
//			getSession().setAttribute(UserConstants.CURRENT_USER, currentUser);
//		}
//	}
//
//	public boolean isUserLoggedIn() {
//		return null != getSession().getAttribute(UserConstants.CURRENT_USER);
//	}

	

	public HttpSession getSession() {
		ExternalContext exCtx =FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) exCtx.getRequest();
		return request.getSession();
	}

	public HttpServletRequest getRequest() {
		ExternalContext exCtx = FacesContext.getCurrentInstance()
				.getExternalContext();
		return(HttpServletRequest) exCtx.getRequest();

	}

}
