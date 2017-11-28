package de.liliane.cw.chatclient.client;

import java.beans.Customizer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.fh_dortmund.inf.cw.chat.client.shared.ChatMessageHandler;
import de.fh_dortmund.inf.cw.chat.client.shared.ServiceHandler;
import de.fh_dortmund.inf.cw.chat.client.shared.UserSessionHandler;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementRemote;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatUserRemote;

public class ServiceHandlerImpl extends ServiceHandler
		implements UserSessionHandler, MessageListener, ChatMessageHandler {

	private static ServiceHandlerImpl instance;
	private Context ctx;
	private ChatManagementRemote chatManagement;
	private ChatUserRemote chatUser;
	private JMSContext jmsContext;
	private Topic observerTopic;
	private Topic disconnectTopic;
	private Queue customerRequestQueue;
	// private static String userName;

	private ServiceHandlerImpl() {

		try {
			ctx = new InitialContext();
			chatManagement = (ChatManagementRemote) ctx.lookup(
					"java:global/ChatClient-ear/ChatClient-ejb/ChatManagementBean!de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementRemote");
			chatUser = (ChatUserRemote) ctx.lookup(
					"java:global/ChatClient-ear/ChatClient-ejb/ChatUserBean!de.liliane.cw.chatclient.server.beans.interfaces.ChatUserRemote");
			initializeJMSConnections();
		} catch (NamingException e) {

			e.printStackTrace();
		}

	}

	public static ServiceHandlerImpl getInstance() {
		if (instance == null) {
			instance = new ServiceHandlerImpl();
		}
		return instance;
	}

	private void initializeJMSConnections() {
		try {
			// common
			ConnectionFactory connectionFactory = (ConnectionFactory) ctx
					.lookup("java:comp/defaultJMSConnectionFactory");
			jmsContext = connectionFactory.createContext();

			// Topic
			observerTopic = (Topic) ctx.lookup("java:global/jms/ObserverTopic");
			jmsContext.createConsumer(observerTopic).setMessageListener(this);

			String selector = "name = " + getUserName();
			// DisconnectTopic
			disconnectTopic = (Topic) ctx.lookup("java:global/jms/DisconnectTopic");
			jmsContext.createConsumer(disconnectTopic, selector).setMessageListener(this);

			// Queue
			customerRequestQueue = (Queue) ctx.lookup("java:global/jms/CustomerRequestQueue");

		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}

	@Override
	public void changePassword(String arg0, String arg1) throws Exception {
		chatUser.changePassword(arg0, arg1);

	}

	@Override
	public void delete(String arg0) throws Exception {

		chatUser.delete(arg0);
	}

	@Override
	public void disconnect() {

		chatUser.disconnect();
	}

	@Override
	public int getNumberOfOnlineUsers() {

		return chatManagement.getNumberOfOnlineUsers();
	}

	@Override
	public int getNumberOfRegisteredUsers() {

		return chatManagement.getNumberOfOnlineUsers();
	}

	@Override
	public List<String> getOnlineUsers() {

		return chatManagement.getOnlineUsers();
	}

	@Override
	public String getUserName() {

		return chatUser.getUserName();
	}

	@Override
	public void login(String arg0, String arg1) throws Exception {
		chatUser.login(arg0, arg1);

	}

	@Override
	public void logout() throws Exception {

		chatUser.logout();
	}

	@Override
	public void register(String arg0, String arg1) throws Exception {

		chatUser.register(arg0, arg1);
	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {

			// textMessage.setStringProperty("NAME", userName);
			// �berpr�fen ob, unser Nachricht �ber unser observerTopic
			// eingekommen ist
			if (message.getJMSDestination().equals(observerTopic)) {
				int observerType = message.getIntProperty("OBSERVER_TYPE");
				if (ChatMessageType.TEXT.ordinal() == observerType) {
					ObjectMessage objmessage = (ObjectMessage) message;
					ChatMessage chatmessage = (ChatMessage) objmessage.getObject();

					// Beobachter �ber �ndString text =
					// textMessage.getText();erungen informieren
					setChanged();
					notifyObservers(chatmessage);
				}

				else if (ChatMessageType.LOGIN.ordinal() == observerType
						|| ChatMessageType.LOGOUT.ordinal() == observerType
						|| ChatMessageType.REGISTER.ordinal() == observerType) {
					ObjectMessage objmessage = (ObjectMessage) message;
					ChatMessage chatmessage = (ChatMessage) objmessage.getObject();// bei an-und abmeldung GUI ChatMessage üBER informiert
																					
					// Beobachter �ber �nderungen informieren
					setChanged();
					notifyObservers(chatmessage); // Nachricht wird angezeigt
				}
				else if (ChatMessageType.DISCONNECT.ordinal() == observerType) {
					ObjectMessage objmessage = (ObjectMessage) message;
					ChatMessage chatmessage = (ChatMessage) objmessage.getObject();// bei an-und abmeldung GUI ChatMessage üBER informiert
																					
					// Beobachter �ber �nderungen informieren
					setChanged();
					notifyObservers(chatmessage);
					disconnect();
				}
			}

		} catch (JMSException ex) {
			ex.printStackTrace();

		}

	}

	@Override
	public void sendChatMessage(String message) {

		// String msgeText = generateHash(text);
		try {
			TextMessage textMessage = jmsContext.createTextMessage();
			textMessage.setStringProperty("NAME", this.getUserName());
			textMessage.setIntProperty("OBSERVER_TYPE", ChatMessageType.TEXT.ordinal());
			textMessage.setText(message);

			jmsContext.createProducer().send(customerRequestQueue, textMessage);

			// ChatMessage chatMessage = new ChatMessage(text, sender, text,
			// date);

		} catch (Exception ex) {

			try {
				throw new Exception("Ihre Nachricht k�nnte nicht verschickt werden.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

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

}
