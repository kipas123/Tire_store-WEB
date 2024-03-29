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
	private static final String PAGE_LOGIN_EDIT = "loginView";
	private static final String PAGE_ADMIN_EDIT = "/pages/admin/index?faces-redirect=true";
	private static final String PAGE_INDEX_EDIT = "/public/index";
	private User user = new User();
	private User userLogin;

	public User getUser() {
		return user;
	}

	public String loginPage() {
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
		if (RemoteClient.load(session) == null)
			return PAGE_LOGIN_EDIT;
		else
			return PAGE_INDEX_EDIT;

	}

	public String adminPage() {
		return PAGE_ADMIN_EDIT;
	}

	public String figlerPage() {
		return "/public/figler";
	}

	public String indexPage() {
		return PAGE_INDEX_EDIT;
	}

	public String login() {
		PasswordAuthentication pa = new PasswordAuthentication();
		userLogin = userDAO.getbyUsername(user.getLogin());
		// 1.Search user in DB
		if(this.userLogin==null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect login or password", null));
			return null;
		}
		if (pa.authenticate(user.getPassword().toCharArray(), userLogin.getPassword())) {

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
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logged in", null));
			return PAGE_INDEX_EDIT;
			// and enter the system (now SecurityFilter will pass the request)
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect login or password", null));
			return null;
		}
	}

//	public  isInanyrole() {
//		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(true);
//
//		if(RemoteClient.load(session)!=null) return null;
//		else return PAGE_INDEX_EDIT;
//	}

	public String doLogout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		// Invalidate session
		// - all objects within session will be destroyed
		// - new session will be created (with new ID)
		session.invalidate();
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Logged out!", null));
		return PAGE_INDEX_EDIT;
	}

}
