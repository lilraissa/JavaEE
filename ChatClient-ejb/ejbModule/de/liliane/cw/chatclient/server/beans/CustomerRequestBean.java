package de.liliane.cw.chatclient.server.beans;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;

import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;

@MessageDriven(mappedName = "java:global/jms/CustomerRequestQueue", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationTYpe", propertyValue = "javax.jms.Queue") })
public class CustomerRequestBean implements MessageListener {
	
	@Inject
	private JMSContext jmsContext;
	@Resource(lookup="java:global/jms/ObserverTopic")
	private Topic observerTopic;
	private final static String[] liste={"scheisse","Schei�arsch", "Scheisligkeit"}; 

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			//�berpr�fen ob Message von Type Text ist
			int observerType = message.getIntProperty("OBSERVER_TYPE");
			if (ChatMessageType.TEXT.ordinal() == observerType) {
				//Textmessage zur�ckerhalten
				TextMessage textMessage = (TextMessage) message;
				ChatMessage chatmessage = new ChatMessage(ChatMessageType.TEXT, textMessage.getStringProperty("NAME"),
						pruefen(textMessage.getText()), new Date());
				ObjectMessage objmessage = jmsContext.createObjectMessage();
				objmessage.setIntProperty("OBSERVER_TYPE", ChatMessageType.TEXT.ordinal());
				objmessage.setObject(chatmessage);
				
				//Message an allen Clients schicken
				jmsContext.createProducer().send(observerTopic, objmessage);
				

			}
			
			// ChatMessage chatMessage = new Message
			// CustomerRequest customerequest = new CustomerRequest();
			// message.setIntProperty("OBSERVER_TYPE",
			// ChatMessageType.TEXT.ordinal());
			// jmsContext.createProducer().send(observerTopic, message);

		} catch (Exception ex) {
			System.err.println("Error while notify observers via topic: " + ex.getMessage());
		}

	}
	//schimpwöter prüfen
	public String pruefen(String msge){
		for(int i=0; i<liste.length;i++ ){
			msge = msge.replace(liste[i], "***");
		}
		return msge;
	}
	
	

}
