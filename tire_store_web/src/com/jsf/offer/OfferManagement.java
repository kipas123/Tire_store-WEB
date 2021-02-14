package com.jsf.offer;


import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;


import java.util.List;

import tire_store.dao.OfferDAO;
import tire_store.dao.TireproductDAO;
import tire_store.entities.Offer;
import tire_store.entities.Tireproduct;


@Named
@ViewScoped
public class OfferManagement implements Serializable {
	@Inject
	FacesContext ctx;
	@Inject
	Flash flash;
	private static final String PAGE_OFFERMANAGEMENT = "/pages/moderator/offerManagement?faces-redirect=true";
	private Offer offer = new Offer();
	private Tireproduct loaded = null;
	private Offer loaded2 = null;
	private int selected;
	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	@EJB
	TireproductDAO tireproductDAO;
	@EJB
	OfferDAO offerDAO;
	public String offerManagementPage() {
		return PAGE_OFFERMANAGEMENT;

	}
	public void onLoad() throws IOException {
		// 1. load person passed through session
		// HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		// loaded = (Person) session.getAttribute("person");
		// 2. load person passed through flash
		loaded = (Tireproduct) flash.get("tireproduct");
		loaded2 = offerDAO.get(loaded);
		// cleaning: attribute received => delete it from session
		if (loaded2 != null) {
			offer = loaded2;
			// session.removeAttribute("person");
		} else {
			// if (!context.isPostback()) { //possible redirect
			// context.getExternalContext().redirect("personList.xhtml");
			// context.responseComplete();
			// }
		}
		
	}
	
	public void offerChange() {
		try {
		if(this.selected == 0 || this.selected == 1) {
			this.offer.setActive((byte)this.selected);
			offerDAO.update(this.offer);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Zapisano!", null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.getExternalContext().redirect("productsManagement.xhtml");
		}
		
		}catch(Exception e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "B³¹d u¿ycia systemu", null));
		}
	}

	
}
