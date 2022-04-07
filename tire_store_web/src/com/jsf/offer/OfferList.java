package com.jsf.offer;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.simplesecurity.RemoteClient;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import tire_store.dao.TireproductDAO;
import tire_store.dao.TiretypeDAO;
import tire_store.entities.Storeorder;
import tire_store.entities.Tireproduct;
import tire_store.entities.User;

@Named
@ViewScoped
public class OfferList implements Serializable {
	@Inject
	FacesContext ctx;

	@Inject
	Flash flash;
	
	@EJB
	TireproductDAO tireproductDAO;
	@EJB
	TiretypeDAO tiretypeDAO;
	private String tiretype;
	private String search;
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	private int lastPage = 1;
	private int page = 1;
	private int pager;
	public int getPager() {
		return pager;
	}

	public String getTiretype() {
		return tiretype;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setTiretype(String tiretype) {
		this.tiretype = tiretype;
	}

	public void onLoad() throws IOException {
		int activeProducts;
		this.pager = page+1;
		if(this.search!=null) {
			if(this.search.equals("null")) this.search=null;
		}
		if(this.search == null) {
			activeProducts = tireproductDAO.countActiveProducts(this.tiretype);
		}else {
			activeProducts = tireproductDAO.countActiveProducts(this.tiretype, this.search);
		}
		
		
		
		
		if(activeProducts>5) {
			if(activeProducts%5==0) {
				this.lastPage=(int)(activeProducts/5);
			}else this.lastPage=(int)(activeProducts/5)+1;
		}else{
			this.lastPage=1;
		}
		if(this.page>=this.lastPage) {
			ctx.getExternalContext().redirect("http://localhost:8080/tire_store/public/index.xhtml");
		}
		
	}
	
	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	private static final String PAGE_OFFERLIST = "/public/offerlist/car?faces-redirect=true";

	public String offerlistPage() {
		return PAGE_OFFERLIST;

	}

	public String accountPage() {
		return "/pages/user/account?faces-redirect=true";

	}

	public List<Tireproduct> getActiveProducts() throws IOException {
		if(this.tiretype==null)return null;
		if(this.search!=null) {
			if(this.search.equals("null")) this.search=null;
		}
		if(this.search!=null) {
			if(this.tiretype.equals("car")) {
				return tireproductDAO.getActiveProducts(tiretypeDAO.get(1), this.page*5, 5, this.search);
			}else if(this.tiretype.equals("truck")) {
				
				return tireproductDAO.getActiveProducts(tiretypeDAO.get(2), this.page*5, 5, this.search);
			}else {
				ctx.getExternalContext().redirect("http://localhost:8080/tire_store/public/index.xhtml");
				return null;
			}
		}else {
			
			if(this.tiretype.equals("car")) {
				return tireproductDAO.getActiveProducts(tiretypeDAO.get(1), this.page*5, 5);
			}else if(this.tiretype.equals("truck")) {
				
				return tireproductDAO.getActiveProducts(tiretypeDAO.get(2), this.page*5, 5);
			}else {
				ctx.getExternalContext().redirect("http://localhost:8080/tire_store/public/index.xhtml");
				return null;
			}
		}
		
	}

	public String offerDetails(Tireproduct tireproduct) {
		// 1. Pass object through session
		// HttpSession session = (HttpSession) extcontext.getSession(true);
		// session.setAttribute("offer", offer);

		// 2. Pass object through flash
		flash.put("tireproduct", tireproduct);

		return "/public/offerView?faces-redirect=true";
	}
	
	public boolean isFirst(){
		if(this.page == 0)return false;
		else return true;
	}
	public boolean isLast(){
		if(this.pager >= this.lastPage)return false;
		else return true;
	}
}
