package com.ctrip.di.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ctrip.di.common.authenticate.LdapAuthenticate;
import com.ctrip.di.common.authenticate.MessageDigestAuthenticate;
import com.ctrip.di.common.util.PrintWriterUtil;
import com.ctrip.di.dao.user.AuthUser;
import com.ctrip.di.dao.user.AuthUserMapper;

/**
 * API to authenticate the user by username and password Example:
 * http://192.168.81
 * .177:8089/di-data-service/auth/authenticate?username=test_xg&password=test
 * 
 * @author xgliao
 * 
 */
@Controller
@RequestMapping("/auth")
public class AuthUserController {
	private static Log logger = LogFactory.getLog(AuthUserController.class);

	@Autowired
	private AuthUserMapper authUserMapper;

	@Autowired
	private LdapAuthenticate ldapAuthenticate;

	@Autowired
	private MessageDigestAuthenticate mdAuthenticate;

	@RequestMapping("/authenticate")
	public void authenticate(HttpServletRequest request,
			HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username == null || password == null) {
			PrintWriterUtil.writeError(request, response,
					"username or password can not be null");
			return;
		}
		AuthUser authUser = authUserMapper.getAuthUser(username);
		if (authUser == null) {
			PrintWriterUtil.writeError(request, response,
					"username is not exist");
			return;
		}

		boolean isValid;
		if (authUser.getPassword().startsWith("!")) {
			isValid = ldapAuthenticate.authenticate(username, password);
		} else {
			isValid = mdAuthenticate.authenticate(username, password);
		}

		response.setContentType("application/json");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.println("{\"isvalid\":" + isValid + "}");
		} catch (IOException e) {
			logger.error("Json Write Error:", e);
		} finally {
			if (pw != null) {
				pw.flush();
				pw.close();
			}
		}

	}

}
