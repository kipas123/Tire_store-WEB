package com.jsf.offer;

import java.io.IOException;
import java.io.Serializable;
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

import tire_store.dao.OfferDAO;
import tire_store.entities.Offer;

@Named
@ViewScoped
public class OfferDetails implements Serializable {
	
	@EJB
	OfferDAO offerDAO;
	
	@Inject
	FacesContext ctx;
	
	@Inject
	Flash flash;

	private static final String PAGE_OFFERVIEW = "/public/offerView?faces-redirect=true";
	private Offer loaded = null;
	private Offer offer = new Offer();
	private Double quantity = 0.0;
	
	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	
	public String indexPage() {
		return "/public/index?faces-redirect=true";

	}
	
	
	public String offerViewPage() {
		return PAGE_OFFERVIEW;

	}
	
	public void onLoad() throws IOException {
		if (!ctx.isPostback()) {
			if (!ctx.isValidationFailed()) {
				loaded = offerDAO.get(offer.getIdoffer());
			}
			if (loaded != null) {
				offer = loaded;
			} else {
				ctx.getExternalContext().redirect("/tire_store");
				// if (!context.isPostback()) { // possible redirect
				// context.getExternalContext().redirect("personList.xhtml");
				// context.responseComplete();
				// }
			}
		}
		// 1. load person passed through session
		// HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		// loaded = (Person) session.getAttribute("person");
		// 2. load person passed through flash
		//loaded = (Offer) flash.get("offer");
		
		// cleaning: attribute received => delete it from session
//		if (loaded != null) {
//			this.offer = loaded;
//			// session.removeAttribute("person");
//		} else {
//			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "B³¹d u¿ycia systemu!", null));
//			// if (!context.isPostback()) { //possible redirect
//			// context.getExternalContext().redirect("personList.xhtml");
//			// context.responseComplete();
//			// }
//		}
		
	}
	
	
	public List<Offer> getList(){
		return offerDAO.getOffer((byte)1);
	}
	
	public Double sumprice(){
		return this.quantity*2;
	}
}
