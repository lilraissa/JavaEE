package de.liliane.cw.chatclient.server.beans.interfaces;

import javax.ejb.Remote;

@Remote
public interface StatisticMessageRemote {
	void createTimer(long duration);
}
