package de.fh_dortmund.inf.cw.chat.server.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@NamedQueries({
	@NamedQuery(name="UserStatistic.geUserStat", query="SELECT us FROM UserStatistic us WHERE us.user = :user_id"),
	@NamedQuery(name="UserStatistic.all", query="SELECT us FROM UserStatistic us")
})

@Entity
//@PrimaryKeyJoinColumn(name="US_PK")
public class UserStatistic extends Statistic{
	
	private static final long serialVersionUID = 1L;
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private long id;
	
	@Basic(optional=false)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private  Date lastLogin;
	
	@OneToOne
	@JoinColumn(name="ID", unique=true)
	private User user;

	public UserStatistic(int logins, int logouts, int messages, Date lastLogin) {
		super(logins, logouts, messages);
		this.lastLogin = lastLogin;
		
	}

	public UserStatistic() {	
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

}
