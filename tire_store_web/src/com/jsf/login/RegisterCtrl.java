package com.jsf.login;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import tire_store.dao.RoleDAO;
import tire_store.dao.UserDAO;
import tire_store.entities.User;

@Named
@RequestScoped
public class RegisterCtrl {
	private static final String PAGE_REGISTER_EDIT = "registerView?faces-redirect=true";
	@Inject
	FacesContext ctx;

	@EJB
	UserDAO userDAO;
	@EJB
	RoleDAO roleDAO;
	private User user = new User();
	private String confPass;

	public User getUser() {
		return user;
	}

	public void setConfPass(String confPass) {
		this.confPass = confPass;
	}

	public String getConfPass() {
		return confPass;
	}

	public void register() {

	}

	public String registerPage() {
		return PAGE_REGISTER_EDIT;
	}

	public boolean confirmData() {
		try {
			PasswordAuthentication pa = new PasswordAuthentication();
			if (!this.user.getPassword().equals(this.confPass)) {
				String messageText = "Powtórz poprawnie has³o";
				FacesMessage errorMessage = new FacesMessage(messageText);
				errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(errorMessage);
			}
			Date data = new Date();
			user.setPassword(pa.hash(user.getPassword().toCharArray()));
			user.setRole(roleDAO.get(1));
			user.setCreatedBy(this.user.getLogin());
			user.setCreatedDate(data);
			user.setLastmodifiedBy(this.user.getLogin());
			user.setLastmodifiedDate(data);
			userDAO.insert(user);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacja wykonana poprawnie", null));

			return true;

		} catch (ValidatorException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return false;
		} catch (Exception e) {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "B³¹d podczas przetwarzania parametrów", null));
			return false;
		}
	}

	public String register_AJAX() {
		if (confirmData()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Wynik: " + user.getLogin(), null));
		}
		return null;
	}

}
