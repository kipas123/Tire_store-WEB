package com.jsf.account;

import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import tire_store.dao.UserDAO;
import tire_store.entities.User;

@Named
@ViewScoped
public class AccountChangeAdress implements Serializable {
	@Inject
	FacesContext ctx;
	private User loaded;
	private User user = new User();
	private static final String PAGE_ADDRESSPAGE = "/pages/user/addressChange?faces-redirect=true";
	@EJB
	UserDAO userDAO;

	public User getLoaded() {
		return loaded;
	}

	public void setLoaded(User loaded) {
		this.loaded = loaded;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String addressPage() {
		return PAGE_ADDRESSPAGE;

	}

	public void onLoad() throws IOException {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		loaded = (User) RemoteClient.load(session).getDetails(); // if nie ma sesji
		// cleaning: attribute received => delete it from session
		if (loaded != null) {
			this.user = loaded;
			// session.removeAttribute("person");
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System usage error!", null));
			// if (!context.isPostback()) { //possible redirect
			// context.getExternalContext().redirect("personList.xhtml");
			// context.responseComplete();
			// }
		}

	}

	public void addressChange() throws IOException {
		try {
			userDAO.update(this.user);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Saved!", null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.getExternalContext().redirect("account.xhtml");

		} catch (Exception e) {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "System usage error" + e.toString(), null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.getExternalContext().redirect("index.xhtml");
		}
	}

}
