package de.liliane.cw.chatclient.server.beans;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateful;

import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagement;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementLocal;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatUserLocal;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatUserRemote;


@Stateful
public class ChatUserBean implements ChatUserRemote, ChatUserLocal {
	
	private String userName;

	@EJB
	private  ChatManagementLocal  externBean;
	
	
	@Override
	public void register(String userName, String password) throws Exception {
		
		if(userName==null || password==null )
	
		{
			throw new IllegalArgumentException("User can not be null");
		}
		
		String  pwd= externBean.getUsers().get(userName);
		
		if(pwd!=null )
			
		{
			throw new IllegalArgumentException(userName + " User already exists");
		}
		
		externBean.getUsers().put(userName, generateHash(password));
		
	}

	@Override
	public void login(String userName, String password) throws Exception {
		if(userName==null || password==null )
	
		{
			throw new IllegalArgumentException("User can not be null");
		}
		String  pwd= externBean.getUsers().get(userName);
		if(pwd==null)
		{
			throw new IllegalArgumentException("Not registred User");
		}
		
		if(!pwd.equals( generateHash(password)))
		{
			throw new IllegalArgumentException("Not registred User");
		}
		
		externBean.getOnlineUsers().add(userName);
		
	}

	@Override
	public void logout() throws Exception {
		

		if(this.userName==null )
	
		{
			throw new IllegalArgumentException("User can not be null");
		}
		
		if (externBean.getOnlineUsers().contains(userName)){
			externBean.getOnlineUsers().remove(userName);
		}
		else
			throw new IllegalArgumentException("User is not connected");
	}

	@Override
	public void disconnect() {
		
		// wenn ein Benutzer angemeldet ist und ein anderer sich anmelden möchte , muss der vorheriger sich  zuerst sich disconnect
	}

	@Override
	public void delete(String password) throws Exception {
		if(password==null )
			
		{
			throw new IllegalArgumentException("password can not be null");
		}
		String  pwd= externBean.getUsers().get(userName);
		if(pwd==null)
		{
			throw new IllegalArgumentException("Not registred User");
		}
		
		if(!pwd.equals( generateHash(password)))
		{
			throw new IllegalArgumentException("Not registred User");
		}
		
		externBean.getUsers().remove(userName);
		
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) throws Exception {
		
		String  pwd= externBean.getUsers().get(userName);
		if(pwd==null)
		{
			throw new IllegalArgumentException("Not registred User");
		}
		
		if(!pwd.equals( generateHash(oldPassword)))
		{
			throw new IllegalArgumentException("Not registred User");
		}
		
		externBean.getUsers().put(userName, newPassword);
		
		
	}
	
	public String generateHash(String plaintext) {
		String hash;
		try {
		MessageDigest encoder = MessageDigest.getInstance("SHA-1");
		hash = String.format("%040x", new BigInteger(1, encoder.digest(plaintext.getBytes())));
		} catch (NoSuchAlgorithmException e) {
		hash = null;
		}
		return hash;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
