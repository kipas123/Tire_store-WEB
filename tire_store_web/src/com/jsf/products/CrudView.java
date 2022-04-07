package com.jsf.products;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import java.util.List;

import tire_store.dao.TireproductDAO;
import tire_store.dao.TiretypeDAO;
import tire_store.dao.UserDAO;
import tire_store.entities.Tireproduct;

@Named
@ViewScoped
public class CrudView implements Serializable {
	@Inject
	FacesContext ctx;
	@Inject
	Flash flash;
	@EJB
	TireproductDAO tireproductDAO;
	@EJB
	TiretypeDAO tiretypeDAO;
	private int selected;
	private int selectedActive;

	public int getSelectedActive() {
		return selectedActive;
	}

	public void setSelectedActive(int selectedActive) {
		this.selectedActive = selectedActive;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	private static final String PAGE_PRODUCTSMANAGEMENT = "/pages/moderator/productsManagement?faces-redirect=true";
	private List<Tireproduct> products;

	private Tireproduct selectedProduct;

	private List<Tireproduct> selectedProducts;

	public List<Tireproduct> getProducts() {
		return products;
	}

	public Tireproduct getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(Tireproduct selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public List<Tireproduct> getSelectedProducts() {
		return selectedProducts;
	}

	public void setSelectedProducts(List<Tireproduct> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

	public void openNew() {
		this.selectedProduct = new Tireproduct();
	}

	public void saveProduct() {
		if (tireproductDAO.get(this.selectedProduct.getIdtire()) == null) {
			if (this.selectedProduct.getBrand() != null && this.selectedProduct.getModel() != null
					&& this.selectedProduct.getSize() != null && this.selected > 0) {
				this.selectedProduct.setTiretype(tiretypeDAO.get(this.selected));
				this.selectedProduct.setActive((byte) this.selectedActive);
				tireproductDAO.insert(this.selectedProduct);
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product added!", null));
				PrimeFaces.current().executeScript("PF('manageProductDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
			} else {
				ctx.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter all required fields!", null));
			}
		} else {
			if (this.selectedProduct.getBrand() != null && this.selectedProduct.getModel() != null
					&& this.selectedProduct.getSize() != null && this.selected > 0) {
				this.selectedProduct.setTiretype(tiretypeDAO.get(this.selected));
				this.selectedProduct.setActive((byte) this.selectedActive);
				tireproductDAO.update(this.selectedProduct);
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product Updated!", null));
				PrimeFaces.current().executeScript("PF('manageProductDialog').hide()");
				PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
			} else {
				ctx.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter all required fields!", null));
			}
		}
	}

	public void deleteProduct() {
		this.products.remove(this.selectedProduct);
		this.selectedProduct = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Removed"));
		PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
	}

	public String getDeleteButtonMessage() {
		if (hasSelectedProducts()) {
			int size = this.selectedProducts.size();
			return size > 1 ? size + " products selected" : "1 product selected";
		}

		return "Delete";
	}

	public boolean hasSelectedProducts() {
		return this.selectedProducts != null && !this.selectedProducts.isEmpty();
	}

	public void deleteSelectedProducts() {
		this.products.removeAll(this.selectedProducts);
		this.selectedProducts = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Products Removed"));
		PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
		PrimeFaces.current().executeScript("PF('dtProducts').clearFilters()");
	}

	public boolean isEdited() {
		if (this.selectedProduct == null)
			return false;
		if (this.selectedProduct.getIdtire() == 0) {
			return false;
		}
		return true;
	}

}
