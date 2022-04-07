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
import tire_store.dao.TireproductDAO;
import tire_store.dao.OrderStatusDAO;
import tire_store.dao.TireproductHasOrderDAO;
import tire_store.dao.UserDAO;
import tire_store.entities.Orderstatus;
import tire_store.entities.Storeorder;
import tire_store.entities.Tireproduct;
import tire_store.entities.User;

@Named
@ViewScoped
public class OrderCtrl implements Serializable {
	@Inject
	FacesContext ctx;
	@Inject
	Flash flash;
	private String search;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		flash.put("search", search);
		this.search = search;
	}

	private int counter;
	private List<Storeorder> orderFiltr;

	public List<Storeorder> getOrderFiltr() {
		return orderFiltr;
	}

	public void setOrderFiltr(List<Storeorder> orderFiltr) {
		this.orderFiltr = orderFiltr;
	}

	private int selected;

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	private double quantity;
	private User loaded;
	private Tireproduct loadedOffer = null;
	private Double totalPrice;
	private User user = new User();
	private Tireproduct tireproduct = new Tireproduct();
	private Storeorder order = new Storeorder();
	private static final String PAGE_SUMMARYPAGE = "/pages/user/orderSummary?faces-redirect=true";
	private static final String PAGE_ORDERSMANAGEMENT = "/pages/moderator/ordersManagement.xhtml?faces-redirect=true";
	private static final String PAGE_ORDERSMANAGEMENTALL = "/pages/moderator/ordersManagementAll?faces-redirect=true";
	private static final String PAGE_ORDERMODIFY = "/pages/moderator/orderModify?faces-redirect=true";
	private static final String PAGE_ORDERSHOW = "/pages/user/orderShow?faces-redirect=true";

	@EJB
	UserDAO userDAO;
	@EJB
	StoreorderDAO orderDAO;
	@EJB
	OrderStatusDAO orderStatusDAO;
	@EJB
	TireproductHasOrderDAO tireproductHasOrderDAO;
	@EJB
	TireproductDAO tireproductDAO;

	public Tireproduct getTireproduct() {
		return tireproduct;
	}

	public void setTireproduct(Tireproduct tireproduct) {
		this.tireproduct = tireproduct;
	}

	public User getLoaded() {
		return loaded;
	}

	public void setLoaded(User loaded) {
		this.loaded = loaded;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String orderSummaryPage() {
		return PAGE_SUMMARYPAGE;

	}

	public String ordersManagement() {
		return PAGE_ORDERSMANAGEMENT;
	}

	public String ordersManagementAll() {
		return PAGE_ORDERSMANAGEMENTALL;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void onLoad() throws IOException {
		if (!ctx.isPostback()) {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			loaded = (User) RemoteClient.load(session).getDetails(); // if nie ma sesji
			this.loadedOffer = (Tireproduct) flash.get("tireproduct");
			this.quantity = (double) flash.get("quantity");
			// cleaning: attribute received => delete it from session
			if (loaded != null && loadedOffer != null && loadedOffer.getDescription() != null) {
				this.user = loaded;
				this.tireproduct = loadedOffer;
				this.totalPrice = this.quantity * this.tireproduct.getPrice();
				// session.removeAttribute("person");
			} else {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System usage error!", null));
				ctx.getExternalContext().redirect("http://localhost:8080/tire_store/public/offerList.xhtml");
				// if (!context.isPostback()) { //possible redirect
				// context.getExternalContext().redirect("personList.xhtml");
				// context.responseComplete();
				// }
			}
		}
	}

	public void buyProduct() throws IOException {
		Date data = new Date();
		this.order.setUser(this.user);
		this.order.setName(this.user.getName());
		this.order.setSurname(this.user.getSurname());
		this.order.setCity(this.user.getCity());
		this.order.setStreet(this.user.getStreet());
		this.order.setHousenumber(this.user.getHousenumber());
		this.order.setZipcode(this.user.getZipcode());
		this.order.setPhoneNumber(this.user.getPhoneNumber());
		this.order.setOrderdata(data);
		this.order.setOrderstatus(orderStatusDAO.get(3));
		this.order.setTotalprice(this.totalPrice);
		this.tireproduct.setQuantity(this.tireproduct.getQuantity() - (int) this.quantity);
		try {
			orderDAO.insert(this.order);
			tireproductDAO.update(this.tireproduct);
			this.order = orderDAO.getLastOrderId();
			tireproductHasOrderDAO.insert(this.loadedOffer.getIdtire(), this.order.getIdorder(), (int) this.quantity);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order was placed correctly!", null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.getExternalContext().redirect("userOrders.xhtml");
		} catch (Exception e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System usage error", null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.getExternalContext().redirect("index.xhtml");
		}

	}

	public String orderModify(Storeorder order) {

		// 1. Pass object through session
		// HttpSession session = (HttpSession) extcontext.getSession(true);
		// session.setAttribute("person", person);

		// 2. Pass object through flash
		flash.put("storeorder", order);

		return PAGE_ORDERMODIFY;
	}

	public String orderShow(Storeorder order) {

		// 1. Pass object through session
		// HttpSession session = (HttpSession) extcontext.getSession(true);
		// session.setAttribute("person", person);

		// 2. Pass object through flash
		flash.put("storeorder2", order);

		return PAGE_ORDERSHOW;
	}

	public String changeOrderStatus(Storeorder order) throws IOException {
		try {
			if (this.selected == 0)
				throw new Exception("Exception message");
			order.setOrderstatus(orderStatusDAO.get(this.selected));
			orderDAO.update(order);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Status changed", null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);
		} catch (Exception e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System usage error", null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.getExternalContext().redirect("index.xhtml");
		}
		return PAGE_ORDERSMANAGEMENT;
	}

	public List<Orderstatus> getOrderStatuses() {
		return orderStatusDAO.getOrderStatuses();
	}

	public String toStringID() {
		this.counter++;
		Integer a = this.counter;
		return "numer" + a.toString();
	}

}
