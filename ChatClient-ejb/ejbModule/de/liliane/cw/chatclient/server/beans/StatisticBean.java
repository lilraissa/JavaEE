package de.liliane.cw.chatclient.server.beans;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;


@Singleton
@Startup // Bean sollte direkt von Application Server gestartet
public class StatisticBean {
	
	@Resource
	private TimerService timerService;
	
	private final String MAIL_STATISTIC_TIMER = "MAIL_STATISTIC_TIMER";

	@PostConstruct
	public void init() {
		

		boolean createTimer = true;
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

			// Interval-Duration 1H = 3600 Seconds
			// Intervall Timer
			timerService.createIntervalTimer(initialExpirationCalendar.getTime(), 3600, timerConfig);
		}
	}

	@Timeout
	public void timeout(Timer timer) {
		if (MAIL_STATISTIC_TIMER.equals(timer.getInfo())) {
			Calendar currentDateCalendar = new GregorianCalendar();
			currentDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
			currentDateCalendar.set(Calendar.MINUTE, 0);
			currentDateCalendar.set(Calendar.SECOND, 0);
			currentDateCalendar.set(Calendar.DAY_OF_MONTH, 0);

			// jai pas fini limplementation
		}

	}


}
