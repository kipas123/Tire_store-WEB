package com.jsf.offer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import tire_store.dao.OfferDAO;
import tire_store.entities.Offer;

@Named
@ViewScoped
public class OfferList implements Serializable {


	@EJB
	OfferDAO offerDAO;
	
	@Inject
	FacesContext ctx;

	private static final String PAGE_OFFERLIST = "/public/offerList?faces-redirect=true";

	public String offerlistPage() {
		return PAGE_OFFERLIST;

	}
	
	public List<Offer> getList(){
		return offerDAO.getOffer((byte)1);
	}
}
