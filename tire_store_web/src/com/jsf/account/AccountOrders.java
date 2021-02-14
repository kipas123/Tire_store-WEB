package com.jsf.account;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
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
import com.jsf.order.OrderCtrl;

import tire_store.dao.OfferDAO;
import tire_store.dao.StoreorderDAO;
import tire_store.dao.TireproductHasOrderDAO;
import tire_store.dao.OrderStatusDAO;
import tire_store.dao.UserDAO;
import tire_store.entities.Offer;
import tire_store.entities.Storeorder;
import tire_store.entities.TireproductHasOrder;
import tire_store.entities.User;

@Named
@ViewScoped
public class AccountOrders implements Serializable {
	@Inject
	FacesContext ctx;
	private static final String PAGE_AccountOrders = "/pages/user/userOrders?faces-redirect=true";
	@Inject
	OrderCtrl orderCtrl;
	
	@EJB
	StoreorderDAO orderDAO;
	@EJB
	OrderStatusDAO orderStatusDAO;
	@EJB
	TireproductHasOrderDAO tireproductHasOrder;
	
	private Storeorder order = new Storeorder();
	//private Order order2 = new Order();
	public String AccountOrdersPage() {
		return PAGE_AccountOrders;

	}
	
	
	
	public List <Storeorder> getUserOrders() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true); //if nie ma sesji
		User user = new User();
		user = (User)RemoteClient.load(session).getDetails();
		return orderDAO.getUserOrders(user);
		
	}
	public List <TireproductHasOrder>getUserOrdersProducts(Storeorder order){
		return tireproductHasOrder.getUserOrdersProducts(order);
	}
	
	
	
	
	
	
}
 
