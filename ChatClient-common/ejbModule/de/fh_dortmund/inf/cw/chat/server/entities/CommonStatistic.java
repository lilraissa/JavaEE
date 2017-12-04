package de.fh_dortmund.inf.cw.chat.server.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@NamedQueries({
	@NamedQuery(name="CommonStatistic.all", query="SELECT cs FROM CommonStatistic cs")
})
@Entity
public class CommonStatistic extends  Statistic{
	
	private static final long serialVersionUID = 1L;
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private long id;
	
	@Basic(optional=false)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date startingDate;
	
	@Basic(optional=false)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date endDate;
	
	
	public CommonStatistic(int logins, int logouts, int messages, Date startingDate, Date endDate) {
		super(logins, logouts, messages);
		this.startingDate = startingDate;
		this.endDate = endDate;
		
	}
	public CommonStatistic() {
	}

//	public long getId() {
//		return id;
//	}

	public Date getStartingDate() {
		return startingDate;
	}


	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	

}
