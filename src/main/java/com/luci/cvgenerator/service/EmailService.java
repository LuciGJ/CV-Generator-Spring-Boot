package com.luci.cvgenerator.service;

import com.luci.cvgenerator.account.EmailDetails;

public interface EmailService {
	public void sendEmail(EmailDetails details);
}
