package de.liliane.cw.chatclient.server.beans.interfaces;

import javax.ejb.Remote;

@Remote
public interface ChatManagementRemote extends ChatManagement{
	void incrementMessageNumber(String user);
}
