package de.liliane.cw.chatclient.server.beans;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;


@MessageDriven(mappedName="java:global/jms/CustomerRequestQueue", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationTYpe", propertyValue= "javax.jms.Queue")
})
public class CustomerRequestBean implements MessageListener{

	@Override
	public void onMessage(Message message) throws JMSException {
		// TODO Auto-generated method stub
		try {
			TextMessage textMessage = (TextMessage) message;
			//CustomerRequest customerequest = new CustomerRequest();
			//message.setIntProperty("OBSERVER_TYPE", ChatMessageType.TEXT.ordinal());
			//jmsContext.createProducer().send(observerTopic, message);
			} catch (JMSException ex) {
			System.err.println("Error while notify observers via topic: " + ex.getMessage());
			}
		
	}

}
