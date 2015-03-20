package com.infostructure.mybigbro.services;

import java.util.Calendar;

import android.util.Log;

public class TimerService {
	
	private DataAccessService dataAccessService;
	long pollFrequencyMillis = 60000; // set default of 1 minute
	long pollFrequencySeconds = 60; // set default of 1 minute
	private long time = 0;

	public TimerService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}
		
	public boolean timerSequenceExpired() {
		try {
			pollFrequencyMillis = dataAccessService.getPollFrequency();
		} catch (Exception e) {
			Log.e("MyBigBro", "Exception : " + e.getMessage());
		}
		long currentTime = Calendar.getInstance().getTime().getTime();
		if (currentTime - time > pollFrequencyMillis) {
			time = currentTime;
			return true;
		}
		return false;
	}
	
	public int getTimerIntervalInSeconds() {
		try {
			return (int) (dataAccessService.getPollFrequency()/1000);
		} catch (Exception e) {
			return (int) pollFrequencySeconds;
		}
	}
}
