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
import tire_store.dao.TireproductDAO;
import tire_store.entities.Tireproduct;

@Named
@ViewScoped
public class OfferManagement implements Serializable {
	@Inject
	FacesContext ctx;
	@Inject
	Flash flash;
	private static final String PAGE_OFFERMANAGEMENT = "/pages/moderator/offerManagement";
	private Tireproduct tireproduct = new Tireproduct();
	private Tireproduct loaded = null;
	private int selected;

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public Tireproduct getTireproduct() {
		return tireproduct;
	}

	public void setTireproduct(Tireproduct tireproduct) {
		this.tireproduct = tireproduct;
	}

	@EJB
	TireproductDAO tireproductDAO;

	public String offerManagementPage() {
		return PAGE_OFFERMANAGEMENT;

	}

	public void onLoad() throws IOException {
		// 1. load person passed through session
		// HttpSession session = (HttpSession)
		// context.getExternalContext().getSession(true);
		// loaded = (Person) session.getAttribute("person");
		// 2. load person passed through flash
		loaded = (Tireproduct) flash.get("tireproduct");
		// cleaning: attribute received => delete it from session
		if (loaded != null) {
			tireproduct = loaded;
			// session.removeAttribute("person");
		} else {
			// if (!context.isPostback()) { //possible redirect
			// context.getExternalContext().redirect("personList.xhtml");
			// context.responseComplete();
			// }
		}

	}

	public void offerChange() throws IOException {
		try {
			if (this.selected == 0 || this.selected == 1) {
				this.tireproduct.setActive((byte) this.selected);
				tireproductDAO.update(this.tireproduct);
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Saved!", null));
				ctx.getExternalContext().getFlash().setKeepMessages(true);
				ctx.getExternalContext().redirect("productsManagement.xhtml");
			}

		} catch (Exception e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System usage error", null));
			ctx.getExternalContext().getFlash().setKeepMessages(true);
			ctx.getExternalContext().redirect("index.xhtml");
		}
	}

}
