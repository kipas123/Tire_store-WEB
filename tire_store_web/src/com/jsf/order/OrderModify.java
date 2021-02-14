package com.jsf.order;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.simplesecurity.RemoteClient;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import tire_store.dao.StoreorderDAO;
import tire_store.dao.TireproductHasOrderDAO;
import tire_store.dao.OfferDAO;
import tire_store.dao.OrderStatusDAO;
import tire_store.entities.Offer;
import tire_store.entities.Storeorder;
import tire_store.entities.Tireproduct;
import tire_store.entities.User;


@Named
@ViewScoped
public class OrderModify implements Serializable {
	@Inject
	FacesContext ctx;
	private static final String PAGE_ORDERMODIFY = "/pages/moderator/orderModify?faces-redirect=true";
	@Inject
	Flash flash;
	private Storeorder order;
	private Storeorder loaded;
	private Storeorder loadedOrder;
	private User loaded_user;
	private int quantity;
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Storeorder getOrder() {
		return order;
	}

	public void setOrder(Storeorder order) {
		this.order = order;
	}

	public Storeorder getLoaded() {
		return loaded;
	}

	public void setLoaded(Storeorder loaded) {
		this.loaded = loaded;
	}

	@EJB
	StoreorderDAO orderDAO;
	@EJB
	OfferDAO offerDAO;
	@EJB
	TireproductHasOrderDAO tireproductHasOrder;
	public String orderModifyPage() {
		return PAGE_ORDERMODIFY;
	}
	
	public void onLoad() throws IOException {
		if (!ctx.isPostback()) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		this.loaded =(Storeorder) flash.get("storeorder");
		// cleaning: attribute received => delete it from session
		if (loaded != null ) {
			this.order = loaded;
			// session.removeAttribute("person");
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "B³¹d u¿ycia systemu!", null));
			ctx.getExternalContext().redirect("http://localhost:8080/tire_store/public/offerList.xhtml");
			// if (!context.isPostback()) { //possible redirect
			// context.getExternalContext().redirect("personList.xhtml");
			// context.responseComplete();
			// }
		}
		}
	}
	
	
	public void onLoad2() throws IOException {
		if (!ctx.isPostback()) {
		this.loadedOrder =(Storeorder) flash.get("storeorder2");
		// cleaning: attribute received => delete it from session
		if (this.loadedOrder != null ) {
			this.order = loadedOrder;
			// session.removeAttribute("person");
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "B³¹d u¿ycia systemu!", null));
			ctx.getExternalContext().redirect("http://localhost:8080/tire_store/public/offerList.xhtml");
			// if (!context.isPostback()) { //possible redirect
			// context.getExternalContext().redirect("personList.xhtml");
			// context.responseComplete();
			// }
		}
		}
	}
	
	
	
	
	
	public Offer getList(){
		Tireproduct tr;
		this.quantity = tireproductHasOrder.getUserOrdersProducts(this.order).get(0).getQuantity();
		tr = (Tireproduct)tireproductHasOrder.getUserOrdersProducts(this.order).get(0).getTireproduct();
		
		return offerDAO.get(tr);
	}
	public void orderModify() throws IOException {
		Date data = new Date();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		loaded_user = (User)RemoteClient.load(session).getDetails(); //if nie ma sesji
		this.order.setLastmodifiedDate(data);
		this.order.setLastmodifiedBy(loaded_user.getLogin());
		orderDAO.update(this.order);
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Pomyœlnie zmodyfikowano!", null));
		ctx.getExternalContext().getFlash().setKeepMessages(true);
		ctx.getExternalContext().redirect("ordersManagementAll.xhtml");
		
		
	}
	
	
	
	
}
 
