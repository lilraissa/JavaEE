package de.liliane.cw.chatclient.server.beans;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagement;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementLocal;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatUserLocal;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatUserRemote;


@Stateful
public class ChatUserBean implements ChatUserRemote, ChatUserLocal {
	
	private String userName;

	@Inject
	private JMSContext jmsContext;
	@Resource(lookup="java:global/jms/ObserverTopic")
	private Topic observerTopic;
	
	//Topic bei  disconnect
	@Resource(lookup="java:global/jms/DisconnectTopic")
	private Topic DisconnectTopic;

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
		this.userName = userName; 
		externBean.getUsers().put(userName, generateHash(password));
		
		notifyViaObserverTopic(ChatMessageType.REGISTER, null);
		
	}

	@Override
	public void login(String userName, String password) throws Exception {
		if(userName==null || password==null )
	
		{
			throw new IllegalArgumentException("User can not be null");
		}
		//haspmap cle valeur qui prend lutilisateur en cle et retourne le mdp coe valeur
		String  pwd= externBean.getUsers().get(userName);
		if(pwd==null)
		{
			throw new IllegalArgumentException("Not registred User");
		}
		
		if(!pwd.equals( generateHash(password)))
		{
			throw new IllegalArgumentException("Not registred User");
		}
		
		//prüfen, ob benutzer schon angemeldet ist
		if (externBean.getOnlineUsers().contains(userName)){
			ChatMessage chatmessage = new ChatMessage(ChatMessageType.DISCONNECT, userName,"Ihre Verbindung wurde getrennt", new Date());
					
			ObjectMessage objmessage = jmsContext.createObjectMessage();
			
			objmessage.setObject(chatmessage);
			//selektor nur bei header und property 
			objmessage.setStringProperty("name", userName); // setProperty nur für diesen user
			//Message an allen Clients schicken
			jmsContext.createProducer().send(DisconnectTopic, objmessage);
		}
		
		externBean.getOnlineUsers().add(userName);
		
		notifyViaObserverTopic(ChatMessageType.LOGIN, "angemeldet");
		
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
		
		notifyViaObserverTopic(ChatMessageType.LOGOUT, "angemeldet");
	}

	@Override
	public void disconnect() {
		if (externBean.getOnlineUsers().contains(userName)){
			externBean.getOnlineUsers().remove(userName);
		}
		
		// wenn ein Benutzer angemeldet ist und ein anderer sich anmelden möchte , muss der vorheriger sich  zuerst sich disconnect
		//notifyViaObserverTopic();
		
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
	
	public void notifyViaObserverTopic(ChatMessageType type, String msge){
		try {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("hh:mm");
			String s = String.format(userName + "hat sich um " + format.format(date) + " Uhr %."  , msge);
			ObjectMessage objmessage = jmsContext.createObjectMessage();
			objmessage.setIntProperty("OBSERVER_TYPE", type.ordinal());
			ChatMessage chatmessage = new ChatMessage(type, userName, s, date);
			objmessage.setObject(chatmessage);
			jmsContext.createProducer().send(observerTopic, objmessage);
			
			} catch (JMSException ex) {
			System.err.println("Error while notify observers via topic: " + ex.getMessage());
			}
	}


}
