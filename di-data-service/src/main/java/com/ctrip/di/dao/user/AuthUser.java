package com.ctrip.di.dao.user;

/**
 * Authenticate user data object
 * @author xgliao
 *
 */
public class AuthUser {
	private String userName;
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User name is " + userName + ", password is " + password;
	}

}
