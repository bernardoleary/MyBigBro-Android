package com.infostructure.mybigbro.model;

public class UserCredentials {
	
	private String mUserName;
	private String mPassword;
	
	public UserCredentials() { }
	
	public UserCredentials(String userName, String password) {
		this.mUserName = userName;
		this.mPassword = password;
	}
	
	public String getUserName() {
		return mUserName;
	}
	
	public void setUserName(String userName) {
		this.mUserName = userName;
	}
	
	public String getPassword() {
		return mPassword;
	}
	
	public void setPassword(String password) {
		this.mPassword = password;
	}
}
