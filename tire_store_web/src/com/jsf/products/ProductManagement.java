package com.jsf.products;


import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import tire_store.dao.OfferDAO;
import tire_store.dao.TireproductDAO;
import tire_store.dao.TiretypeDAO;
import tire_store.dao.UserDAO;
import tire_store.entities.Orderstatus;
import tire_store.entities.Tireproduct;
import tire_store.entities.Tiretype;


@Named
@ViewScoped
public class ProductManagement implements Serializable {
	@Inject
	FacesContext ctx;
	@Inject
	Flash flash;
	private static final String PAGE_PRODUCTSMANAGEMENT = "/pages/moderator/productsManagement?faces-redirect=true";
	private static final String PAGE_OFFERMANAGEMENT = "/pages/moderator/offerManagement?faces-redirect=true";
	
	@EJB
	TireproductDAO tireproductDAO;
	@EJB
	TiretypeDAO tiretypeDAO;
	@EJB
	OfferDAO offerDAO;
	public String productsPage() {
		return PAGE_PRODUCTSMANAGEMENT;

	}
	
	public List<Tireproduct> getAllProducts(){
		return tireproductDAO.getAllProducts();
	}
	
	public List<Tiretype> getTiretypes(){
		return tiretypeDAO.getTiretypes();
	}
	
	public Boolean toBollean(Tireproduct tireproduct) {
		if(tireproduct==null) return false;
		if(tireproduct.getHasOffer()==1) {
			return true;
		}else return false;
	}
	public Boolean toBollean2(Tireproduct tireproduct) {
		if(tireproduct==null) return false;
		
		if(offerDAO.get(tireproduct)!=null && offerDAO.get(tireproduct).getActive()==1) {
			
			return true;
		}else return false;
	}
	public String editOffer(Tireproduct tireproduct){
		//1. Pass object through session
		//HttpSession session = (HttpSession) extcontext.getSession(true);
		//session.setAttribute("person", person);
		
		//2. Pass object through flash 
		flash.put("tireproduct", tireproduct);
		
		return PAGE_OFFERMANAGEMENT;
	}
	
}
