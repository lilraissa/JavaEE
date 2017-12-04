package de.liliane.cw.chatclient.server.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

import de.fh_dortmund.inf.cw.chat.server.entities.CommonStatistic;
import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessage;
import de.fh_dortmund.inf.cw.chat.server.shared.ChatMessageType;
import de.liliane.cw.chatclient.server.beans.interfaces.ChatManagementLocal;
import de.liliane.cw.chatclient.server.beans.interfaces.StatisticMessageLocal;
import de.liliane.cw.chatclient.server.beans.interfaces.StatisticMessageRemote;

@Stateless
public class StatisticMessageBean implements StatisticMessageLocal, StatisticMessageRemote {

	@Inject
	private JMSContext jmsContext;
	@Resource(lookup = "java:global/jms/ObserverTopic")
	private Topic observerTopic;

	@Resource
	private TimerService timerService;

	@EJB
	private ChatManagementLocal chatManagement;

	private final String MY_MAIL_STATISTIC_TIMER = "MY_MAIL_STATISTIC_TIMER";

	public void createTimer(long duration) {
		TimerConfig timerConfig = new TimerConfig();
		timerConfig.setInfo(MY_MAIL_STATISTIC_TIMER);
		timerConfig.setPersistent(true); // Default value

		// Intervall-Timer
		Calendar initialExpirationCalendar = new GregorianCalendar();
		// initialExpirationCalendar.set(Calendar.HOUR_OF_DAY, 0);
		initialExpirationCalendar.add(Calendar.MINUTE, 30);
		// initialExpirationCalendar.set(Calendar.SECOND, 0);
		// initialExpirationCalendar.set(Calendar.DAY_OF_MONTH, 0);

		// Interval-Duration halbe Stunde = 1800 Seconds
		// Intervall Timer
		timerService.createIntervalTimer(initialExpirationCalendar.getTime(), duration, timerConfig);
	}

	@Timeout
	public void timeout(Timer timer) {
		if (MY_MAIL_STATISTIC_TIMER.equals(timer.getInfo())) {
			Calendar currentDateCalendar = new GregorianCalendar();

			// Datum aktueller Tag
			// currentDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
			// currentDateCalendar.set(Calendar.MINUTE, 0);
			currentDateCalendar.set(Calendar.SECOND, 0);
			// currentDateCalendar.set(Calendar.DAY_OF_MONTH, 0);

			Calendar startingDateCalendar = new GregorianCalendar();
			startingDateCalendar.setTime(currentDateCalendar.getTime());
			startingDateCalendar.add(Calendar.MINUTE, -30); // 30 Minuten vorher
			Date startingDate = startingDateCalendar.getTime();

			Calendar endDateCalendar = new GregorianCalendar();
			endDateCalendar.setTime(currentDateCalendar.getTime());
			endDateCalendar.add(Calendar.MILLISECOND, -1); // eine Millisekonde
															// weniger
			Date endDate = endDateCalendar.getTime();

			sendMessage(startingDate, endDate, "halben Stunde");
		}
	}

	@Schedule(hour = "*/1")
	public void timeout() {
		// Datum aktueller Tag
		// if (MAIL_STATISTIC_TIMER.equals(timerService.getInfo())) {

		System.out.println("TIMER TO MAKE DETAUL STATS");
		Calendar currentDateCalendar = new GregorianCalendar();
		// currentDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
		// currentDateCalendar.set(Calendar.MINUTE, 0);
		// currentDateCalendar.set(Calendar.SECOND, 0);
		// currentDateCalendar.set(Calendar.DAY_OF_MONTH, 0);

		Calendar startingDateCalendar = new GregorianCalendar();
		startingDateCalendar.setTime(currentDateCalendar.getTime());
		startingDateCalendar.add(Calendar.HOUR_OF_DAY, -1); // 1 Stunde
															// vorher
		Date startingDate = startingDateCalendar.getTime();

		Calendar endDateCalendar = new GregorianCalendar();
		endDateCalendar.setTime(currentDateCalendar.getTime());
		endDateCalendar.add(Calendar.MILLISECOND, -1); // eine Millisekonde
														// weniger
		Date endDate = endDateCalendar.getTime();

		sendMessage(startingDate, endDate, "Stunde");
		// }
	}

	private void sendMessage(Date startingDate, Date endDate, String time) {
		try {
			ObjectMessage objMessage = jmsContext.createObjectMessage();
			objMessage.setIntProperty("OBSERVER_TYPE", ChatMessageType.STATISTIC.ordinal());

			int loging = 0;
			int logout = 0;
			int messages = 0;

			// Map<String, UserStatistic> userStats =
			// chatManagement.getAllStatistics();
			// Iterator<String> it = userStats.keySet().iterator();
			// while (it.hasNext()) {
			// UserStatistic stat = userStats.get(it.next());
			// if (startingDate.compareTo(stat.getLastLogin()) <= 0 &&
			// endDate.compareTo(stat.getLastLogin()) >= 0) {
			// loging += stat.getLogins();
			// logout += stat.getLogouts();
			// messages += stat.getMessages();
			// }
			// }

			List<UserStatistic> statistics = chatManagement.getAllUserStatistics();
			for (UserStatistic stat : statistics) {
				if (startingDate.compareTo(stat.getLastLogin()) <= 0 && endDate.compareTo(stat.getLastLogin()) >= 0) {
					loging += stat.getLogins();
					logout += stat.getLogouts();
					messages += stat.getMessages();
				}
			}

			Date date = new Date();

			String text = "  Statistik der letzten " + time;
			text += "\n  Anzahl der Anmeldungen: " + loging;
			text += "\n  Anzahl der Abmeldungen:" + logout;
			text += "\n  Anzahl der geschriebenen Nachrichten: " + messages;

			ChatMessage chatMsg = new ChatMessage(ChatMessageType.STATISTIC, "Automatisch Timer", text, date);

			objMessage.setObject(chatMsg);

			jmsContext.createProducer().send(observerTopic, objMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
