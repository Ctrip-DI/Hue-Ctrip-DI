package com.ctrip.di.hive.alert;

import org.junit.Test;

public class TestEMail {
	
	@Test
	public void test() throws Exception {
		String subject = "测试邮件";

		String content = "有任何疑问可以发邮件到 cdi-hadoop@Ctrip.com";

		EMail.sendMail(subject, content, "xgliao@ctrip.com", null);
	}

}
