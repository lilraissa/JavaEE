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

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.inf.cw.chat.server.entities.Statistic;
import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementLocal;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementRemote;

@Singleton
@Startup
public class ChatManagementBean implements ChatManagementRemote, ChatManagementLocal {

	@Inject
	private JMSContext jmsContext;
	@Resource(lookup = "java:global/jms/ObserverTopic")
	private Topic observerTopic;

	

	// private String userName;
	// private int numberOfRegisteredUsers;
	// private int numberOfOnlineUsers;
	private Map<String, String> users;
	private List<String> onlineUsers;
	private long lastItemNummbers = 0;
	
	private Map<String, UserStatistic> allStatistics;
	private List<CommonStatistic> commonStatistics;
	//private List<Statistic> statistics;
	
	@PostConstruct
	public void init() {
		users = new HashMap<String, String>();
		onlineUsers = new ArrayList<String>();
		allStatistics = new HashMap<String, UserStatistic>();
		commonStatistics = new ArrayList<>();
		//statistics = new ArrayList<>();
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



	@Override
	public Map<String, UserStatistic> getAllStatistics() {
		return allStatistics;
	}



	@Override
	public List<CommonStatistic> getCommonStatistics() {
		return commonStatistics;
	}



//	@Override
//	public List<Statistic> getStatistics() {
//		return statistics;
//	}

}
