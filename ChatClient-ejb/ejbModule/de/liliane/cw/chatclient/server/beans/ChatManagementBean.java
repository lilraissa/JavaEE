package de.liliane.cw.chatclient.server.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.sun.xml.ws.developer.Stateful;

import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementLocal;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementRemote;
import de.liliane.cw.chatclient.server.entities.User;

@Singleton
@Startup
public class ChatManagementBean implements ChatManagementRemote, ChatManagementLocal {
	
	//private String  userName;
	//private int numberOfRegisteredUsers;
	//private int numberOfOnlineUsers;
	private Map<String, String> users;
	private  List<String> onlineUsers;
	private long lastItemNummbers=0;
	
	
	@PostConstruct
	public void init(){
		users=new HashMap<String, String>(); 
		onlineUsers=new  ArrayList<String>(); 
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
