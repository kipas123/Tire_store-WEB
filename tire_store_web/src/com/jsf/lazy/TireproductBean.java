package com.jsf.lazy;

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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.jsf.order.OrderCtrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tire_store.dao.StoreorderDAO;
import tire_store.dao.TireproductDAO;
import tire_store.dao.TiretypeDAO;
import tire_store.dao.UserDAO;
import tire_store.entities.Storeorder;
import tire_store.entities.Tireproduct;

@Named(value = "tireproductBean")
@ViewScoped
public class TireproductBean implements Serializable {

	@Inject
	OrderCtrl orderCtrl;
	private List<Tireproduct> orderList;
	private LazyDataModel<Tireproduct> model;
	private String search;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	/**
	 * Creates a new instance of PlayerBean
	 */
	public TireproductBean() {

	}

	@EJB
	TireproductDAO tireproductDAO;

	@SuppressWarnings("serial")
	@PostConstruct
	public void init() {

		model = new LazyDataModel<Tireproduct>() {

			public List<Tireproduct> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				model.setRowCount(count(search));
				orderList = getResultList(offset, pageSize, search);
				return orderList;
			}
		};
	}

	public LazyDataModel<Tireproduct> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Tireproduct> model) {
		this.model = model;
	}

	private List<Tireproduct> getResultList(int offset, int pageSize, String search) {
		if (search != null)
			return tireproductDAO.getProducts(search, offset, pageSize);
		return tireproductDAO.getProducts(offset, pageSize);
	}

	private int count(String search) {
		if (this.search != null)
			return tireproductDAO.countProducts(search);
		return tireproductDAO.countProducts();
	}

}
