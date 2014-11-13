package com.ctrip.di.common.authenticate;

/**
 * 
 * Authenticate, provide a API for user to authenticate if the user is valid
 * in di portal, if it is valid, return ture, or false. 
 * @author xgliao
 *
 */
public interface IAuthenticate {

	boolean authenticate(String username, String password);

}
