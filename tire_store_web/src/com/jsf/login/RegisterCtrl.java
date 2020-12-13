package com.jsf.login;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import tire_store.dao.RoleDAO;
import tire_store.dao.UserDAO;
import tire_store.entities.User;

@Named
@RequestScoped
public class RegisterCtrl {
	private static final String PAGE_REGISTER_EDIT = "registerView?faces-redirect=true";
	@Inject
	FacesContext ctx;

	@EJB
	UserDAO userDAO;
	@EJB
	RoleDAO roleDAO;
	private User user = new User();
	private String username;
	private String password;
	private String confPass;
	private String name;
	private String surname;
	private String city;
	private String street;
	private String zipCode;
	private String houseNumber;
	private int number;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfPass() {
		return confPass;
	}

	public void setConfPass(String confPass) {
		this.confPass = confPass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void register() {

	}
	
	
	public String registerPage() {
		return PAGE_REGISTER_EDIT;
	}
	
	
	
	

	public boolean doTheMath() {
		try {
			if(!this.password.equals(this.confPass)) {
			String messageText = "Powtórz poprawnie has³o";
		    FacesMessage errorMessage = new FacesMessage(messageText);
			errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(errorMessage);
			}
			Date data = new Date();
			user.setLogin(this.username);
			user.setPassword(this.password);
			user.setRole(roleDAO.get(1));
			user.setName(this.name);
			user.setSurname(this.surname);
			user.setCity(this.city);
			user.setStreet(this.street);
			user.setHousenumber(this.houseNumber);
			user.setZipcode(this.zipCode);
			user.setCreatedBy(this.username);
			user.setCreatedDate(data);
			user.setLastmodifiedBy(this.username);
			user.setLastmodifiedDate(data);
			userDAO.insert(user);
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Operacja wykonana poprawnie", null));
			
			return true;

		}catch (ValidatorException e) {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return false;
		} catch (Exception e) {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "B³¹d podczas przetwarzania parametrów", null));
			return false;
		}
	}

	public String calc_AJAX() {
		if (doTheMath()) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Wynik: " + getUsername(), null));
		}
		return null;
	}


}
