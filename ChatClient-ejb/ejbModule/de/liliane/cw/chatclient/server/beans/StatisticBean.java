package de.liliane.cw.chatclient.server.beans;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementLocal;


@Singleton
@Startup // Bean sollte direkt von Application Server gestartet
public class StatisticBean {
	
	@Resource
	private TimerService timerService;
	
	@EJB
	private ChatManagementLocal chatManagement;
	
	private final String MAIL_STATISTIC_TIMER = "MAIL_STATISTIC_TIMER";

	@PostConstruct
	public void init() {
		

		boolean createTimer = true; // zum Begin der Methode init , soll ueberprueft werden ob der Timer schon laeuft
		// check existings Timers
		for (Timer timer : timerService.getTimers()) {
			if (MAIL_STATISTIC_TIMER.equals(timer.getInfo())) {
				createTimer = false;
				break;
			}

		}

		// Timer erzeugen
		if (createTimer) {
			TimerConfig timerConfig = new TimerConfig();
			timerConfig.setInfo(MAIL_STATISTIC_TIMER);
			timerConfig.setPersistent(true); // Default value

			// Intervall-Timer
			Calendar initialExpirationCalendar = new GregorianCalendar();
			initialExpirationCalendar.set(Calendar.HOUR_OF_DAY, 0);
			initialExpirationCalendar.set(Calendar.MINUTE, 0);
			initialExpirationCalendar.set(Calendar.SECOND, 0);
			initialExpirationCalendar.set(Calendar.DAY_OF_MONTH, 0);

			// Interval-Duration halbe Stunde = 3600 Seconds
			// Intervall Timer
			timerService.createIntervalTimer(initialExpirationCalendar.getTime(), 1800*1000, timerConfig);
		}
	}

	// wird aufgerufen wenn ein Timer event auftritt
	@Timeout
	public void timeout(Timer timer) {
		if (MAIL_STATISTIC_TIMER.equals(timer.getInfo())) {
			Calendar currentDateCalendar = new GregorianCalendar();
			
			//Datum aktueller Tag
			currentDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
			currentDateCalendar.set(Calendar.MINUTE, 0);
			currentDateCalendar.set(Calendar.SECOND, 0);
			currentDateCalendar.set(Calendar.DAY_OF_MONTH, 0);
			
			Calendar startingDateCalendar =  new GregorianCalendar();
			startingDateCalendar.setTime(currentDateCalendar.getTime());
			startingDateCalendar.add(Calendar.HOUR_OF_DAY, -1); // eine Stunde vorher
			Date startingDate = startingDateCalendar.getTime();
			
			Calendar endDateCalendar =  new GregorianCalendar();
			endDateCalendar.setTime(currentDateCalendar.getTime());
			endDateCalendar.add(Calendar.MILLISECOND, -1);  // eine Minute weniger
			Date endDate = endDateCalendar.getTime();
			int loging = 0;
			int logout = 0;
			int messages = 0;
			
			Map<String, UserStatistic> userStats = chatManagement.getAllStatistics();
			Iterator<String> it = userStats.keySet().iterator();
			while (it.hasNext()) {
				UserStatistic stat = userStats.get(it.next());
				loging += stat.getLogins();
				loging += stat.getLogouts();
				messages += stat.getMessages();
			}
			
			CommonStatistic commomStat = new CommonStatistic();
			commomStat.setStartingDate(startingDate);
			commomStat.setEndDate(endDate);
			commomStat.setLogins(loging);
			commomStat.setLogouts(logout);
			commomStat.setMessages(messages);
			
			chatManagement.getCommonStatistics().add(commomStat);
		}

	}


}
