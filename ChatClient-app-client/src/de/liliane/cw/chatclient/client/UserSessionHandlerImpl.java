package de.liliane.cw.chatclient.client;

import java.util.List;

import de.fh_dortmund.inf.cw.chat.client.shared.ServiceHandler;
import de.fh_dortmund.inf.cw.chat.client.shared.UserSessionHandler;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementRemote;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatUserRemote;

public class UserSessionHandlerImpl {
	
	private ChatManagementRemote chatManagement;
	private ChatUserRemote chatUser;
	
	
	private UserSessionHandlerImpl(ChatManagementRemote chatManagement,ChatUserRemote chatUser){
		this.chatManagement=chatManagement;
		this.chatUser=chatUser;
	}
/*
	@Override
	public void changePassword(String arg0, String arg1) throws Exception {
		chatUser.changePassword(arg0, arg1);

	}

	@Override
	public void delete(String arg0) throws Exception {
		
		chatUser.delete(arg0);
	}

	@Override
	public void disconnect() {
		
		chatUser.disconnect();
	}

	@Override
	public int getNumberOfOnlineUsers() {
		
		return chatManagement.getNumberOfOnlineUsers();
	}

	@Override
	public int getNumberOfRegisteredUsers() {
		
		return  chatManagement.getNumberOfOnlineUsers();
	}

	@Override
	public List<String> getOnlineUsers() {
		
		return chatManagement.getOnlineUsers();
	}

	@Override
	public String getUserName() {
		
		return chatUser.getUserName();
	}

	@Override
	public void login(String arg0, String arg1) throws Exception {
		chatUser.login(arg0, arg1);

	}

	@Override
	public void logout() throws Exception {
		
		chatUser.logout();
	}

	@Override
	public void register(String arg0, String arg1) throws Exception {
		
		chatUser.register(arg0, arg1);
		
	}
*/
}
