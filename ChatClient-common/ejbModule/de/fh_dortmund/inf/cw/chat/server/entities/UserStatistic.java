package de.fh_dortmund.inf.cw.chat.server.entities;

import java.util.Date;

public class UserStatistic extends Statistic{
	private  Date lastLogin;

	public UserStatistic(int logins, int logouts, int messages, Date lastLogin) {
		super(logins, logouts, messages);
		this.lastLogin = lastLogin;
		
	}
	
	public UserStatistic(UserStatistic stat) {
		if(stat != null) {
			this.setLogins(stat.getLogins());
			this.setLogouts(stat.getLogins());
			this.setMessages(stat.getMessages());
		}
	}
	
	public UserStatistic() {
		
		
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	

}
