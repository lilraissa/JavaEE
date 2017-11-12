package de.liliane.cw.chatclient.server.beans.interfaces;

import java.util.List;
import java.util.Map;

public interface ChatManagement {

	List<String> getOnlineUsers();

	int getNumberOfRegisteredUsers();

	int getNumberOfOnlineUsers();
	
	Map<String, String> getUsers();
}
