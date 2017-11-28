package de.liliane.cw.chatclient.server.entities;

public class CustomerRequest {
	Customer customer;
	String message;
	
	public CustomerRequest(){
		
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

}
