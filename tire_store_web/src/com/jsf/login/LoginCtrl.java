package com.jsf.login;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import tire_store.dao.UserDAO;

@Named
@ViewScoped
public class LoginCtrl implements Serializable {

	
	
	@EJB
	UserDAO userDAO;
	private static final String PAGE_LOGIN_EDIT = "loginView?faces-redirect=true";
	private static final String PAGE_INDEX_EDIT = "index";
	public String loginPage() {
		return PAGE_LOGIN_EDIT;
	}
	public String indexPage() {
		return PAGE_INDEX_EDIT;
	}
	
}
