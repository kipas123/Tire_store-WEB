package com.jsf.account;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import tire_store.dao.StoreorderDAO;
import tire_store.dao.TireproductHasOrderDAO;
import tire_store.entities.Storeorder;
import tire_store.entities.TireproductHasOrder;
import tire_store.entities.User;

@Named
@ViewScoped
public class AccountOrders implements Serializable {
	@Inject
	FacesContext ctx;
	private static final String PAGE_AccountOrders = "/pages/user/userOrders?faces-redirect=true";
	@EJB
	StoreorderDAO orderDAO;
	@EJB
	TireproductHasOrderDAO tireproductHasOrderDAO;

	// private Order order2 = new Order();
	public String AccountOrdersPage() {
		return PAGE_AccountOrders;
	}

	public List<TireproductHasOrder> getUserOrdersProducts(Storeorder order) {
		return tireproductHasOrderDAO.getUserOrdersProducts(order);
	}

}
