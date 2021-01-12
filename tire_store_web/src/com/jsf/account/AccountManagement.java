package com.jsf.account;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.simplesecurity.RemoteClient;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import tire_store.dao.OfferDAO;
import tire_store.dao.UserDAO;
import tire_store.entities.Offer;
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
		if(RemoteClient.load(session)!=null) {
			User user = (User)RemoteClient.load(session).getDetails();
			if(user.getPhoneNumber()!=0) return true;
		} 
		
		return false;
	}
	
	private boolean checkPass(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		if(RemoteClient.load(session)!=null) {
			this.user = (User)RemoteClient.load(session).getDetails();
			if(user.getPassword().equals(this.getPass())) {
				return true; 
			}else ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Stare haslo niepoprawne", null));
	}
		return false;
	}
	
	public void changePass() {
		if(this.checkPass() && this.getNewpass().equals(this.getConfPass())) {
			user.setPassword(this.getConfPass());
			userDAO.update(user);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Zmieniono has³o", null));
		}else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Powtórzone has³o ró¿ni siê od nowego", null));
		}
		
		
	}
	
	public static boolean islogged() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		if(RemoteClient.load(session)==null) return false;
		else return true;

	}
 
}
