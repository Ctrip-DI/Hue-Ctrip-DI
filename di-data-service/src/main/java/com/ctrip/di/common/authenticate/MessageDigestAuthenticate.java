package com.ctrip.di.common.authenticate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctrip.di.dao.user.AuthUser;
import com.ctrip.di.dao.user.AuthUserMapper;

/**
 * Message Digest authenticate.
 * Currently, there is two type of users in di portal. One is Ldap user,
 * and the other is hadoop user.
 * @author xgliao
 *
 */
@Service
public class MessageDigestAuthenticate implements IAuthenticate {
	private static Log logger = LogFactory
			.getLog(MessageDigestAuthenticate.class);

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	@Autowired
	private AuthUserMapper authUserMapper;

	@Override
	public boolean authenticate(String username, String password) {
		AuthUser user = authUserMapper.getAuthUser(username);
		String[] parts = user.getPassword().split("\\$");
		if (parts.length != 3) {
			logger.info("User password format is not right, while user is "
					+ username);
			return false;
		}
		
		String digestPassword = getDigestPassword(parts[0], parts[1], password);
		if(digestPassword != null && digestPassword.equalsIgnoreCase(parts[2])) {
			return true;
		}

		return false;
	}

	public String getDigestPassword(String algorithm, String salt,
			String password) {
		MessageDigest meesageDigest = null;
		try {
			meesageDigest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			logger.warn("Message Digest Exception:", e);
			return null;
		}
		meesageDigest.update((salt + password).getBytes());

		return getFormattedText(meesageDigest.digest());
	}

	private String getFormattedText(byte[] bytes) {
		int len = bytes.length;

		StringBuilder buf = new StringBuilder(len * 2);

		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}

}
