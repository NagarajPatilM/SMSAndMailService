package com.tyss.smsandmailservice.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

@Service
public class EmailServiceImpl implements EmailService {

	@Override
	public com.tyss.smsandmailservice.dto.Response sendEmail(String from, List<Email> tos, String message,
			List<Email> ccs) {

		com.tyss.smsandmailservice.dto.Response responsee = new com.tyss.smsandmailservice.dto.Response();
		Personalization personalization = new Personalization();
		Email fro = new Email(from);
		String subject = message;

		Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
		// Mail mail = new Mail(fro, subject, t, content);
		Mail mail = new Mail();
		mail.setSubject(subject);
		mail.setFrom(fro);
		personalization.addTo(tos.get(0));
		mail.addContent(content);
		mail.addPersonalization(personalization);

		for (int i = 1; i < tos.size(); i++) {
			mail.personalization.get(0).addTo(tos.get(i));
		}

		for (int i = 0; i < ccs.size(); i++) {
			mail.personalization.get(0).addCc(ccs.get(i));
		}

		SendGrid sg = new SendGrid("");
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			responsee.setStatusCode(201);
			responsee.setMessage("Success");
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
			return responsee;
		} catch (IOException ex) {
			// throw ex;
			System.out.println(ex);
			responsee.setMessage("Exception");
			responsee.setStatusCode(501);
			return responsee;
		}
	}
}
