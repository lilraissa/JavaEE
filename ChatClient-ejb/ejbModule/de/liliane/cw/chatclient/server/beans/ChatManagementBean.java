package de.liliane.cw.chatclient.server.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sun.xml.ws.developer.Stateful;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.inf.cw.chat.server.entities.Statistic;
import de.fh_dortmund.inf.cw.chat.server.entities.User;
import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementLocal;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementRemote;

//@Singleton
//@Startup
@Stateless
public class ChatManagementBean implements ChatManagementRemote, ChatManagementLocal {
	//private Map<String, User> users;
	
	private Map<String, UserStatistic> allStatistics;
	private List<CommonStatistic> commonStatistics;
	
	@PersistenceContext(unitName = "ChatDB")
	private EntityManager entityManager;
	
	@PostConstruct
	public void init() {
		//users = new HashMap<String, String>();
		//onlineUsers = new ArrayList<String>();
		//users = new HashMap<>();
		allStatistics = new HashMap<String, UserStatistic>();
		commonStatistics = new ArrayList<>();
	}
	
	@Override
	public List<String> getOnlineUsers() {
		//return onlineUsers;
		TypedQuery<User> query = entityManager.createNamedQuery("User.onlineList", User.class);
		query.setParameter("online", true);
		List<User> users = query.getResultList();
		List<String> listName = new ArrayList<>();
		for (User user : users) {
			listName.add(user.getName());
		}
		return listName;
	}

	@Override
	public int getNumberOfRegisteredUsers() {
		TypedQuery<Long> query = entityManager.createNamedQuery("User.registerCount", Long.class);
		return query.getSingleResult().intValue();
	}

	@Override
	public int getNumberOfOnlineUsers() {
		TypedQuery<Long> query = entityManager.createNamedQuery("User.onlineCount", Long.class);
		query.setParameter("online", true);
		return query.getSingleResult().intValue();
	}

//	@Override
//	public Map<String, UserStatistic> getAllStatistics() {
//		return allStatistics;
//	}

	@Override
	public List<CommonStatistic> getCommonStatistics() {
		TypedQuery<CommonStatistic> query = entityManager.createNamedQuery("CommonStatistic.all", CommonStatistic.class);
		return query.getResultList();
	}

	@Override
	public void incrementMessageNumber(String user) {
		UserStatistic stat = allStatistics.get(user);
		stat.setMessages(stat.getMessages() + 1);
		allStatistics.put(user, stat);
	}



	@Override
	public User createUser(User user) {
		if(user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		//users.put(user.getName(), user);
		entityManager.persist(user);
		return user;
	}



	@Override
	public User updateUser(User user) {
		if(user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
//		users.put(user.getName(), user);
		entityManager.merge(user);
		return user;
	}



	@Override
	public void deleteUser(User user) {
		if(user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		//users.remove(user.getName());
		entityManager.remove(user);
	}



	@Override
	public User findUser(String userName) {
		if(userName == null) {
			throw new IllegalArgumentException("User name cannot be null");
		}
		try {
			TypedQuery<User> query = entityManager.createNamedQuery("User.find", User.class);
			query.setParameter("name", userName);
			User user = query.getSingleResult();
			
			return user;
		} catch (Exception e) {
			System.out.println("User with name " + userName + " not found.");
		}
		
		return null;
	}

	@Override
	public UserStatistic createUstaticStatistic(UserStatistic statistic) {
		if(statistic == null) {
			throw new IllegalArgumentException("Statistic cannot be null");
		}
		entityManager.persist(statistic);
		return statistic;
	}

	@Override
	public UserStatistic updateUstaticStatistic(UserStatistic statistic) {
		if(statistic == null) {
			throw new IllegalArgumentException("Statistic cannot be null");
		}
		entityManager.persist(statistic);
		return statistic;
	}

	@Override
	public UserStatistic findUstaticStatistic(String userName) {
		if(userName == null) {
			throw new IllegalArgumentException("userName cannot be null");
		}
		try {
			User user = findUser(userName);
			
			TypedQuery<UserStatistic> query = entityManager.createNamedQuery("UserStatistic.geUserStat", UserStatistic.class);
			query.setParameter("user_id", user);
			return query.getSingleResult();
		} catch (Exception e) {
			System.out.println();
		}
		return null;
	}

	@Override
	public void deleteUstaticStatistic(UserStatistic statistic) {
		if(statistic == null) {
			throw new IllegalArgumentException("UserStatistic cannot be null");
		}
		entityManager.remove(statistic);
	}

	@Override
	public List<UserStatistic> getAllUserStatistics() {
		TypedQuery<UserStatistic> query = entityManager.createNamedQuery("UserStatistic.all", UserStatistic.class);
		return query.getResultList();
	}

	@Override
	public CommonStatistic createCommonStatistic(CommonStatistic statistic) {
		if(statistic == null) {
			throw new IllegalArgumentException("CommonStatistic cannot be null");
		}
		entityManager.persist(statistic);
		return statistic;
	}
}
