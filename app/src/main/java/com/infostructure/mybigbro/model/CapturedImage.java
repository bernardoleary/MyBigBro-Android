package com.infostructure.mybigbro.model;

import java.util.Date;

public class CapturedImage {

	private String mUrl;
	private Date mDateTimeCaptured;
	
	public String getUrl()
	{
		return this.mUrl;
	}
	
	public Date getDateTimeCaptured()
	{
		return this.mDateTimeCaptured;
	}
	
	public void setUrl(String url)
	{
		this.mUrl = url;
	}
	
	public void setDateTimeCaptured(Date dateTimeCaptured)
	{
		this.mDateTimeCaptured = dateTimeCaptured;
	}
}
