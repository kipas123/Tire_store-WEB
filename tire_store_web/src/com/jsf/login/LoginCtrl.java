package com.jsf.login;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import tire_store.dao.UserDAO;
import tire_store.entities.User;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class LoginCtrl {

	@Inject
	FacesContext ctx;

	@EJB
	UserDAO userDAO;
	private static final String PAGE_LOGIN_EDIT = "loginView?faces-redirect=true";
	private static final String PAGE_ADMIN_EDIT = "/pages/admin/index?faces-redirect=true";
	private static final String PAGE_INDEX_EDIT = "/public/index";
	private User user = new User();
	private User userLogin;

	public User getUser() {
		return user;
	}

	public String loginPage() {
		return PAGE_LOGIN_EDIT;
	}

	public String adminPage() {
		return PAGE_ADMIN_EDIT;
	}

	public String indexPage() {
		return PAGE_INDEX_EDIT;
	}

	public String login() {
		// 1.Search user in DB
		if (loginAuthentication()) {

			// 2. if logged in: get User roles, save in RemoteClient and store it in session

			RemoteClient<User> client = new RemoteClient<User>(); // create new RemoteClient
			client.setDetails(this.userLogin);

			String role = userLogin.getRole().getName(); // get User roles

			if (role != null) { // save roles in RemoteClient
				client.getRoles().add(role);
			}

			// store RemoteClient with request info in session (needed for SecurityFilter)
			HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
			client.store(request);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Zalogowano", null));
			return PAGE_INDEX_EDIT;
			// and enter the system (now SecurityFilter will pass the request)
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Niepoprawny login lub has³o", null));
			return null;
		}
	}

	private boolean loginAuthentication() {

		userLogin = userDAO.getloginAccount(user.getLogin(), user.getPassword());
		if (this.userLogin != null) {
			return true;
		} else {
			return false;
		}

	}
//	public String isInrole() {
//		HttpSession session;
//		session.getAttribute;
//		session.
//		RemoteClient<User> client = new RemoteClient<User>();
//		client.load(session);
//		client.isInRole2();
//		return null;
//	}

	public String doLogout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		// Invalidate session
		// - all objects within session will be destroyed
		// - new session will be created (with new ID)
		session.invalidate();
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wylogowano!", null));
		return PAGE_INDEX_EDIT;
	}

}
