package de.liliane.cw.chatclient.server.beans;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.inf.cw.chat.server.entities.User;
import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementLocal;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatUserLocal;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatUserRemote;


@Stateful
public class ChatUserBean implements ChatUserRemote, ChatUserLocal {
	
	private String userName;
	private User user;
	
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
		
		User user = externBean.findUser(userName);
		//String  pwd= externBean.getUsers().get(userName);
		
		if(user!=null )
			
		{
			throw new IllegalArgumentException(userName + " User already exists");
		}
		user = new User(userName, generateHash(password));
		this.userName = userName; 

		this.user = externBean.createUser(user);
		
		//externBean.getUsers().put(userName, generateHash(password));
		
		notifyViaObserverTopic(ChatMessageType.REGISTER, null);
		
	}

	@Override
	public void login(String userName, String password) throws Exception {
		if(userName==null || password==null ){
			throw new IllegalArgumentException("User can not be null");
		}
		//haspmap cle valeur qui prend lutilisateur en cle et retourne le mdp coe valeur
		//String  pwd= externBean.getUsers().get(userName);
		User user = externBean.findUser(userName);
		if(user==null){
			throw new IllegalArgumentException("Not registred User");
		}
		
		if(!user.getPassword().equals( generateHash(password))){
			throw new IllegalArgumentException("User or password is incorrect");
		}
		
		this.userName = userName;
		
		//prüfen, ob benutzer schon angemeldet ist
		if (externBean.getOnlineUsers().contains(userName)){
			ChatMessage chatmessage = new ChatMessage(ChatMessageType.DISCONNECT, userName,"Ihre Verbindung wurde getrennt", new Date());
					
			ObjectMessage objmessage = jmsContext.createObjectMessage();
			objmessage.setIntProperty("OBSERVER_TYPE", ChatMessageType.DISCONNECT.ordinal());
			objmessage.setObject(chatmessage);
			//selektor nur bei header und property 
			objmessage.setStringProperty("name", userName); // setProperty nur für diesen user
			//Message an allen Clients schicken
			jmsContext.createProducer().send(DisconnectTopic, objmessage);
			externBean.getOnlineUsers().add(userName);
		}
		//externBean.getOnlineUsers().add(userName);
		user.setOnline(true);
		this.user = externBean.updateUser(user);
		// Statistik
		notifyViaObserverTopic(ChatMessageType.LOGIN, "angemeldet");
		loginStatistik();
	
	}

	private void loginStatistik() {
		UserStatistic stat = externBean.findUstaticStatistic(userName);
		if(stat == null) {
			stat = new UserStatistic();
			stat.setLastLogin(new Date());
			stat.setLogins(stat.getLogins() + 1);
			stat.setUser(user);
			externBean.createUstaticStatistic(stat);
		}else {
			stat.setLastLogin(new Date());
			stat.setLogins(stat.getLogins() + 1);
			externBean.updateUstaticStatistic(stat);
		}
	}
	
	@Remove
	@Override
	public void logout() throws Exception {
		
		if(this.userName !=null ){
			User user = externBean.findUser(userName);
			user.setOnline(false);
			this.user = externBean.updateUser(user);
			
			logoutStatistik();
			notifyViaObserverTopic(ChatMessageType.LOGOUT, "angemeldet");
		}	
	}

	private void logoutStatistik() {
		UserStatistic stat = externBean.findUstaticStatistic(userName);
		stat.setUser(user);
		stat.setLogouts(stat.getLogouts() + 1);
		externBean.updateUstaticStatistic(stat);
		
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
		//String  pwd= externBean.getUsers().get(userName);
		User user = externBean.findUser(userName);
		if(user==null)
		{
			throw new IllegalArgumentException("Not registred User");
		}
		
		if(!user.getPassword().equals( generateHash(password)))
		{
			throw new IllegalArgumentException("User or password is incorrect");
		}
		externBean.deleteUser(user);
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) throws Exception {
		
		//String  pwd= externBean.getUsers().get(userName);
		User user = externBean.findUser(userName);
		if(user==null)
		{
			throw new IllegalArgumentException("Not registred User");
		}
		if(!user.getPassword().equals(generateHash(oldPassword)))
		{
			throw new IllegalArgumentException("Password is not correct.");
		}
		user.setPassword(generateHash(newPassword));
		externBean.updateUser(user);
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
			String s = String.format(userName + "hat sich um " + format.format(date) + " Uhr %s."  , msge);
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
