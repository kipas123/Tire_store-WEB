package com.jsf.account;

import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.simplesecurity.RemoteClient;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.jsf.login.PasswordAuthentication;

import tire_store.dao.UserDAO;
import tire_store.entities.User;

@Named
@ViewScoped
public class AccountManagement implements Serializable {
	@EJB
	UserDAO userDAO;
	@Inject
	FacesContext ctx;

	@Inject
	Flash flash;
	private String pass;

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getNewpass() {
		return newpass;
	}

	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}

	public String getConfPass() {
		return confPass;
	}

	public void setConfPass(String confPass) {
		this.confPass = confPass;
	}

	private String newpass;
	private String confPass;
	private User user;
	private static final String PAGE_ACCOUNT = "/pages/user/account?faces-redirect=true";

	public String accountPage() {
		return PAGE_ACCOUNT;

	}

	public boolean hasphoneNumber() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		if (RemoteClient.load(session) != null) {
			User user = (User) RemoteClient.load(session).getDetails();
			if (user != null && user.getPhoneNumber() != 0)
				return true;
		}

		return false;
	}

	private boolean checkPass() {
		PasswordAuthentication pa = new PasswordAuthentication();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		if (RemoteClient.load(session) != null) {
			this.user = (User) RemoteClient.load(session).getDetails();
			if (user == null)
				return false;
			if (pa.authenticate(this.getPass().toCharArray(), user.getPassword())) {
				return true;
			} else
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old password not valid", null));
		}
		return false;
	}

	public void changePass() throws IOException {
		PasswordAuthentication pa = new PasswordAuthentication();
		if (this.checkPass() && this.getNewpass().equals(this.getConfPass()) && this.user != null) {
			user.setPassword(pa.hash(this.getConfPass().toCharArray()));
			try {
				userDAO.update(user);
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed", null));
			} catch (Exception e) {
				ctx.getExternalContext().getFlash().setKeepMessages(true);
				ctx.getExternalContext().redirect("index.xhtml");
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System usage error", null));
			}
		} else {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "The repeated password is different from the new one", null));
		}

	}

	public static boolean islogged() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		if (RemoteClient.load(session) == null)
			return false;
		else
			return true;

	}

}
