package de.fh_dortmund.inf.cw.chat.server.entities;

public class Statistic {
	
	private int logins;
	private int logouts;
	private int messages;
	
	public Statistic(int logins, int logouts, int messages ) {
		this.logins = logins;
		this.logouts = logouts;
		this.messages = messages;
	}
	
	public Statistic( ) {
		
	}
	

	public int getLogins() {
		return logins;
	}

	public void setLogins(int logins) {
		this.logins = logins;
	}

	public int getLogouts() {
		return logouts;
	}

	public void setLogouts(int logouts) {
		this.logouts = logouts;
	}

	public int getMessages() {
		return messages;
	}

	public void setMessages(int messages) {
		this.messages = messages;
	}
	
	

}
