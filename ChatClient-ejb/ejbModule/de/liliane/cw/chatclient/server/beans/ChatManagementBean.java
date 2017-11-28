package de.liliane.cw.chatclient.server.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;

import com.sun.xml.ws.developer.Stateful;

import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementLocal;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementRemote;
import de.liliane.cw.chatclient.server.entities.User;

@Singleton
@Startup
public class ChatManagementBean implements ChatManagementRemote, ChatManagementLocal {

	@Inject
	private JMSContext jmsContext;
	@Resource(lookup = "java:global/jms/ObserverTopic")
	private Topic observerTopic;

	@Resource
	private TimerService timerService;

	// private String userName;
	// private int numberOfRegisteredUsers;
	// private int numberOfOnlineUsers;
	private Map<String, String> users;
	private List<String> onlineUsers;
	private long lastItemNummbers = 0;

	private final String MAIL_STATISTIC_TIMER = "MAIL_STATISTIC_TIMER";

	@PostConstruct
	public void init() {
		users = new HashMap<String, String>();
		onlineUsers = new ArrayList<String>();

		boolean createTimer = true;
		// check existings Timers
		for (Timer timer : timerService.getTimers()) {
			if (MAIL_STATISTIC_TIMER.equals(timer.getInfo())) {
				createTimer = false;
				break;
			}

		}

		// Timer erzeugen
		if (createTimer) {
			TimerConfig timerConfig = new TimerConfig();
			timerConfig.setInfo(MAIL_STATISTIC_TIMER);
			timerConfig.setPersistent(true);  //Default value

			// Intervall-Timer
			Calendar initialExpirationCalendar = new GregorianCalendar();
			initialExpirationCalendar.set(Calendar.HOUR_OF_DAY, 0);
			initialExpirationCalendar.set(Calendar.MINUTE, 0);
			initialExpirationCalendar.set(Calendar.SECOND, 0);
			initialExpirationCalendar.set(Calendar.DAY_OF_MONTH, 0);

			// Interval-Duration 1H = 3600 Seconds
			// Intervall Timer
			timerService.createIntervalTimer(initialExpirationCalendar.getTime(), 3600, timerConfig);
		}
	}
	
	@Timeout
	public void timeout(Timer timer) {
		if (MAIL_STATISTIC_TIMER.equals(timer.getInfo())) {
			Calendar currentDateCalendar = new GregorianCalendar();
			currentDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
			currentDateCalendar.set(Calendar.MINUTE, 0);
			currentDateCalendar.set(Calendar.SECOND, 0);
			currentDateCalendar.set(Calendar.DAY_OF_MONTH, 0);
			
			// jai pas fini limplementation
		}
		
	}

	@Override
	public List<String> getOnlineUsers() {

		return onlineUsers;

	}

	@Override
	public int getNumberOfRegisteredUsers() {

		return users.keySet().size();
	}

	@Override
	public int getNumberOfOnlineUsers() {

		return onlineUsers.size();
	}

	public Map<String, String> getUsers() {
		return users;
	}

	public void setUsers(Map<String, String> users) {
		this.users = users;
	}

}
