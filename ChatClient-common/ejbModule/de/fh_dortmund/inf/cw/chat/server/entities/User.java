package de.fh_dortmund.inf.cw.chat.server.entities;

import java.io.Serializable;

import javax.inject.Named;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
	@NamedQuery(name = "User.all", query="SELECT u FROM User u"),
	@NamedQuery(name = "User.find",query="SELECT u FROM User u Where u.name = :name" ),
	@NamedQuery(name = "User.onlineList", query="SELECT u FROM User u Where u.online = :online"),
	@NamedQuery(name = "User.onlineCount", query="SELECT COUNT(u) FROM User u Where u.online = :online"),
	@NamedQuery(name = "User.registerCount", query="SELECT COUNT(u) FROM User u")
})

@Entity
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Basic(optional = false)
	@Column(nullable = false)
	private String name;
	@Basic(optional = false)
	@Column(nullable = false)
	private String password;
	@Column(nullable = false) 
	private boolean online;
	
	public User() {
	}
	public User(String name, String password) {
		this.name = name;
		this.password = password;
		this.online = false;
	}
	
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isOnline() {
		return online;
	}
	
	public void setOnline(boolean online) {
		this.online = online;
	}
}
