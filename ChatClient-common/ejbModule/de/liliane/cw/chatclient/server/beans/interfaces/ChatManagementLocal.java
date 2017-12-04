package de.liliane.cw.chatclient.server.beans.interfaces;

import java.util.List;

import javax.ejb.Local;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.inf.cw.chat.server.entities.User;
import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;

@Local
public interface ChatManagementLocal extends ChatManagement{
	User createUser(User user);
	User updateUser(User user);
	void deleteUser(User user);
	User findUser(String userName);
	
	UserStatistic createUstaticStatistic(UserStatistic statistic);
	UserStatistic updateUstaticStatistic(UserStatistic statistic);
	void deleteUstaticStatistic(UserStatistic statistic);
	List<UserStatistic> getAllUserStatistics();
	
	CommonStatistic createCommonStatistic(CommonStatistic statistic);
}
