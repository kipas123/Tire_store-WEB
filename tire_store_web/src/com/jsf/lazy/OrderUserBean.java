package com.jsf.lazy;

import java.io.Serializable;

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
import tire_store.entities.User;

@Named(value = "orderUserBean")
@ViewScoped
public class OrderUserBean implements Serializable {

	@Inject
	OrderCtrl orderCtrl;
	private List<Storeorder> orderList;
	private LazyDataModel<Storeorder> model;
	private User user;

	/**
	 * Creates a new instance of PlayerBean
	 */
	public OrderUserBean() {

	}

	@EJB
	StoreorderDAO storeorderDAO;

	@SuppressWarnings("serial")
	@PostConstruct
	public void init() {

		model = new LazyDataModel<Storeorder>() {

			public List<Storeorder> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
						.getSession(true);
				user = (User) RemoteClient.load(session).getDetails(); // if nie ma sesji
				model.setRowCount(count(user));
				orderList = getResultList(user, offset, pageSize);
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

	private List<Storeorder> getResultList(User user, int offset, int pageSize) {

		return storeorderDAO.getUserOrders(user, offset, pageSize);
	}

	private int count(User user) {
		return storeorderDAO.countUserOrders(user);
	}

}
