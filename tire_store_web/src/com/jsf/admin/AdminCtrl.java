package com.jsf.admin;

import javax.faces.view.ViewScoped;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import tire_store.dao.RoleDAO;
import tire_store.dao.TireproductDAO;
import tire_store.dao.UserDAO;
import tire_store.entities.Role;
import tire_store.entities.Tireproduct;
import tire_store.entities.User;

@Named
@ViewScoped
public class AdminCtrl implements Serializable {
	@Inject
	FacesContext ctx;
	private static final String PAGE_ADMINPAGE = "/pages/admin/adminCtrl";
	private static final String PAGE_ARCHIVIEDPRODUCTS = "/pages/admin/productsArchivied";
	@EJB
	UserDAO userDAO;
	@EJB
	RoleDAO roleDAO;
	@EJB
	TireproductDAO tireproductDAO;
	private int selected;
	private String login = null;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	// private Order order2 = new Order();
	public String AdminPage() {
		return PAGE_ADMINPAGE;

	}

	public String ArchiviedPage() {
		return PAGE_ARCHIVIEDPRODUCTS;

	}

	public List<User> getAdmins() {
		return userDAO.getAdmins();
	}

	public List<User> getUser() {
		return userDAO.getAdmins(this.login);
	}

	public List<Role> getRoles() {
		return roleDAO.getRoles();
	}

	public String restore(Tireproduct tireproduct) throws IOException {
		try {
			tireproduct.setArchived((byte) 0);
			tireproductDAO.update(tireproduct);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product restored", null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);

		} catch (Exception e) {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "System usage error" + e.toString(), null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.getExternalContext().redirect("index.xhtml");
		}
		return PAGE_ARCHIVIEDPRODUCTS;
	}

	public String changeRole(User user) throws IOException {
		try {
			user.setRole(roleDAO.get(this.selected));
			userDAO.update(user);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User role changed", null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);

		} catch (Exception e) {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "System usage error" + e.toString(), null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.getExternalContext().redirect("index.xhtml");
		}
		return PAGE_ADMINPAGE;
	}

}
