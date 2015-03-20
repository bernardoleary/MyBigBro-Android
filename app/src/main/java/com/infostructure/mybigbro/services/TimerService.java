package com.infostructure.mybigbro.services;

import java.util.Calendar;

import android.util.Log;

public class TimerService {
	
	private DataAccessService mDataAccessService;
	private long mPollFrequencyMillis = 60000; // set default of 1 minute
    private long mPollFrequencySeconds = 60; // set default of 1 minute
	private long mTime = 0;

	public TimerService(DataAccessService dataAccessService) {
		this.mDataAccessService = dataAccessService;
	}
		
	public boolean timerSequenceExpired() {
		try {
            mPollFrequencyMillis = this.mDataAccessService.getPollFrequency();
		} catch (Exception e) {
			Log.e("MyBigBro", "Exception : " + e.getMessage());
		}
		long currentTime = Calendar.getInstance().getTime().getTime();
		if (currentTime - this.mTime > this.mPollFrequencyMillis) {
			this.mTime = currentTime;
			return true;
		}
		return false;
	}
	
	public int getTimerIntervalInSeconds() {
		try {
			return (int) (this.mDataAccessService.getPollFrequency()/1000);
		} catch (Exception e) {
			return (int) this.mPollFrequencySeconds;
		}
	}
}
