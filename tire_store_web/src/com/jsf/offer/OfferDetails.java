package com.jsf.offer;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import tire_store.dao.TireproductDAO;
import tire_store.entities.Tireproduct;

@Named
@ViewScoped
public class OfferDetails implements Serializable {

	@EJB
	TireproductDAO tireproductDAO;

	@Inject
	FacesContext ctx;

	@Inject
	Flash flash;

	private static final String PAGE_OFFERVIEW = "/public/offerView?faces-redirect=true";
	private Tireproduct loaded = null;
	private Tireproduct tireproduct = new Tireproduct();
	private Double quantity = 0.0;

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Tireproduct getTireproduct() {
		return tireproduct;
	}

	public void setTireproduct(Tireproduct tireproduct) {
		this.tireproduct = tireproduct;
	}

	public String indexPage() {
		return "/public/index?faces-redirect=true";

	}

	public boolean hasQuantity() {
		if (this.tireproduct == null)
			return false;
		if (this.tireproduct.getQuantity() <= 0)
			return false;
		return true;
	}

	public String offerViewPage() {
		return PAGE_OFFERVIEW;

	}

	public void onLoad() throws IOException {
		if (!ctx.isPostback()) {
			if (!ctx.isValidationFailed()) {
				loaded = tireproductDAO.get(tireproduct.getIdtire());
			}
			if (loaded != null) {
				tireproduct = loaded;
			} else {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System usage error", null));
				ctx.getExternalContext().getFlash().setKeepMessages(true);
				ctx.getExternalContext().redirect("index.xhtml");
				// if (!context.isPostback()) { // possible redirect
				// context.getExternalContext().redirect("personList.xhtml");
				// context.responseComplete();
				// }
			}
		}
		// 1. load person passed through session
		// HttpSession session = (HttpSession)
		// context.getExternalContext().getSession(true);
		// loaded = (Person) session.getAttribute("person");
		// 2. load person passed through flash
		// loaded = (Tireproduct) flash.get("tireproduct");

		// cleaning: attribute received => delete it from session
//		if (loaded != null) {
//			this.tireproduct = loaded;
//			// session.removeAttribute("person");
//		} else {
//			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "B³¹d u¿ycia systemu!", null));
//			// if (!context.isPostback()) { //possible redirect
//			// context.getExternalContext().redirect("personList.xhtml");
//			// context.responseComplete();
//			// }
//		}

	}

	public Double sumprice() {
		return this.quantity * this.tireproduct.getPrice();
	}

	public String buyProduct(Tireproduct tireproduct) {
		// 1. Pass object through session
		// HttpSession session = (HttpSession) extcontext.getSession(true);
		// session.setAttribute("person", person);

		// 2. Pass object through flash
		flash.put("tireproduct", tireproduct);
		flash.put("quantity", quantity);
		if (quantity > 0) {
			return "/pages/user/orderSummary?faces-redirect=true";
		} else
			return null;

	}
}
