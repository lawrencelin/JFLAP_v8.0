package model.sets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import view.sets.ActiveSetsList;

public class SetsManager {

	public static ActiveSetsRegistry ACTIVE_REGISTRY;

	public SetsManager () {
		ACTIVE_REGISTRY = new ActiveSetsRegistry();
	}
	
	public void setObserveableTarget (ActiveSetsList disp) {
		ACTIVE_REGISTRY.setObserver(disp);
	}
	
	public static String getAutomatedName() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		return System.getProperty("user.name") + format.format(cal.getTime());
	}
}
