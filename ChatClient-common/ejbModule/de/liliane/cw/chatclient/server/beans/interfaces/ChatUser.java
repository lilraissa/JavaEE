package de.liliane.cw.chatclient.server.beans.interfaces;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;

public interface ChatUser {

	void register(String user, String paramString2) throws Exception;

	void login(String paramString1, String paramString2) throws Exception;

	void logout() throws Exception;

	void disconnect();

	void delete(String paramString) throws Exception;

	void changePassword(String paramString1, String paramString2) throws Exception;

	String getUserName();
	UserStatistic getuserStatistic();

}
