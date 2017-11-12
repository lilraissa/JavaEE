package de.liliane.cw.chatclient.server.entities;

public class User {
	
	private String  userName;
	private String  passwort;
	
	public User(String name, String pwd){
		this.userName=name;
		this.passwort=pwd;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPasswort() {
		return passwort;
	}
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

}
