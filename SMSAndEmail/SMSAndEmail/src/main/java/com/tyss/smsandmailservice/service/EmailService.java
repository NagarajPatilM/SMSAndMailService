package com.tyss.smsandmailservice.service;

import java.io.IOException;
import java.util.List;

import com.sendgrid.helpers.mail.objects.Email;
import com.tyss.smsandmailservice.dto.Response;

public interface EmailService {

	public Response sendEmail(String from, List<Email> tos, String message, List<Email> ccs) throws IOException;

}
