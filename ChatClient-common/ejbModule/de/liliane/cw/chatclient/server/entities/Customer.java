package de.liliane.cw.chatclient.server.entities;

public class Customer {
	Person person;
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Customer(Person person){
		this.person = person;
	}
}
