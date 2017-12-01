package de.liliane.cw.chatclient.server.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;

@Stateless
public class AutomaticStatisticBean {
	
	@Inject
	private JMSContext jmsContext;
	@Resource(lookup="java:global/jms/ObserverTopic")
	private Topic observerTopic;
	
	
	@Schedule(hour ="0/30")
	public void timeout() {
		try {
			ObjectMessage objMessage = jmsContext.createObjectMessage();
			objMessage.setIntProperty("OBSERVER_TYPE", ChatMessageType.STATISTIC.ordinal());
			
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			String text = "Statistik um " + format.format(date) + " Uhr";  
			ChatMessage chatMsg = new ChatMessage(ChatMessageType.STATISTIC, "Automatisch Timer", text, date);
			
			objMessage.setObject(chatMsg);
			
			jmsContext.createProducer().send(observerTopic, objMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
