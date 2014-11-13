package com.ctrip.di.common.authenticate;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Ldap authenticate.
 * Currently, there is two type of users in di portal. One is Ldap user,
 * and the other is hadoop user.
 * @author xgliao
 *
 */
@Service
public class LdapAuthenticate implements IAuthenticate {
	private static Log logger = LogFactory
			.getLog(LdapAuthenticate.class);
	
	@Value("${LDAP_URL}")
	private String ldapUrl;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public LdapContext connectLdap(String ldapAccount, String ldapPwd,
			String range) throws NamingException {
		String ldapFactory = "com.sun.jndi.ldap.LdapCtxFactory";
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, ldapFactory);
		env.put(Context.PROVIDER_URL, ldapUrl);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, range + "\\" + ldapAccount);
		env.put(Context.SECURITY_CREDENTIALS, ldapPwd);
		env.put("java.naming.referral", "follow");
		LdapContext ctxTDS = new InitialLdapContext(env, null);
		return ctxTDS;
	}

	public boolean authenticate(String username, String password) {
		String range = "cn1";
		try {
			connectLdap(username, password, range);
			return true;
		} catch (Exception e) {
			logger.warn("Auth error:", e);
			return false;
		}
	}

}
