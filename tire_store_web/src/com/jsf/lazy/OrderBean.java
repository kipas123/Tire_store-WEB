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

@Named(value = "orderBean")
@ViewScoped
public class OrderBean implements Serializable {

	@Inject
	OrderCtrl orderCtrl;
	private List<Storeorder> orderList;
	private LazyDataModel<Storeorder> model;
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
	public OrderBean() {

	}

	@EJB
	StoreorderDAO storeorderDAO;

	@SuppressWarnings("serial")
	@PostConstruct
	public void init() {

		model = new LazyDataModel<Storeorder>() {

			public List<Storeorder> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				model.setRowCount(count(search));
				orderList = getResultList(offset, pageSize, search);
				return orderList;
			}
		};
	}

	public LazyDataModel<Storeorder> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Storeorder> model) {
		this.model = model;
	}

	private List<Storeorder> getResultList(int offset, int pageSize, String search) {
		if (search != null)
			return storeorderDAO.getOrders(search, offset, pageSize);
		return storeorderDAO.getOrders(offset, pageSize);
	}

	private int count(String search) {
		if (this.search != null)
			return storeorderDAO.countOrders(search);
		return storeorderDAO.countOrders();
	}

}
