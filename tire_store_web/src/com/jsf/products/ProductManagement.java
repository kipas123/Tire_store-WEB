package com.jsf.products;

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
import tire_store.dao.TireproductDAO;
import tire_store.dao.TiretypeDAO;
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

	public String productsPage() {
		return PAGE_PRODUCTSMANAGEMENT;

	}

	public List<Tiretype> getTiretypes() {
		return tiretypeDAO.getTiretypes();
	}

	public Boolean toBollean(Tireproduct tireproduct) {
		if (tireproduct == null)
			return false;

		if (tireproductDAO.get(tireproduct.getIdtire()) != null
				&& tireproductDAO.get(tireproduct.getIdtire()).getActive() == 1) {

			return true;
		} else
			return false;
	}

	public String editOffer(Tireproduct tireproduct) {
		// 1. Pass object through session
		// HttpSession session = (HttpSession) extcontext.getSession(true);
		// session.setAttribute("person", person);

		// 2. Pass object through flash
		flash.put("tireproduct", tireproduct);

		return PAGE_OFFERMANAGEMENT;
	}

	public String archiveProduct(Tireproduct tireproduct) throws IOException {
		try {
			tireproduct.setArchived((byte) 1);
			tireproduct.setActive((byte) 0);
			tireproductDAO.update(tireproduct);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Archived product!", null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);
		} catch (Exception e) {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "System usage error" + e.toString(), null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.getExternalContext().redirect("index.xhtml");
		}

		return PAGE_PRODUCTSMANAGEMENT;

	}

}
