package de.liliane.cw.chatclient.server.beans.interfaces;

import java.util.List;
import java.util.Map;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.inf.cw.chat.server.entities.Statistic;
import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;

public interface ChatManagement {

	List<String> getOnlineUsers();

	int getNumberOfRegisteredUsers();

	int getNumberOfOnlineUsers();
	
	Map<String, String> getUsers();
	
	Map<String, UserStatistic> getAllStatistics();
	//List<Statistic> getStatistics();
	List<CommonStatistic> getCommonStatistics();
}
