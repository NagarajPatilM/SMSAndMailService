package com.tyss.smsandmailservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

@Service
public class EmailServiceImpl implements EmailService {

	@Override
	public com.tyss.smsandmailservice.dto.Response sendEmail(String from, List<Email> tos, String message,
			List<Email> ccs) throws IOException {

		com.tyss.smsandmailservice.dto.Response responseBean = new com.tyss.smsandmailservice.dto.Response();
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

		Attachments attachments1 = new Attachments();
		Path file1 = Paths.get("D:\\New Text Document.txt");
		byte[] attachmentContentBytes1 = Files.readAllBytes(file1);
		String attachementContent1 = Base64.getMimeEncoder().encodeToString(attachmentContentBytes1);
		attachments1.setContent(attachementContent1);
		attachments1.setFilename("balance1.txt");
		attachments1.setDisposition("inline");
		attachments1.setContentId("Banner");
		attachments1.setType("application/pdf");
		mail.addAttachments(attachments1);

//		Attachments attachments2 = new Attachments();
//		Path file2 = Paths.get("D:\\new.txt");
//		byte[] attachmentContentBytes2 = Files.readAllBytes(file2);
//		String attachementContent2 = Base64.getMimeEncoder().encodeToString(attachmentContentBytes2);
//		attachments2.setContent(attachementContent2);
//		attachments2.setFilename("balance2.txt");
//		attachments2.setDisposition("inline");
//		attachments2.setContentId("Banner");
//		attachments2.setType("application/pdf");
//		mail.addAttachments(attachments2);

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
			responseBean.setStatusCode(201);
			responseBean.setMessage("Success");
			return responseBean;
		} catch (IOException ex) {
			// throw ex;
			System.out.println(ex);
			responseBean.setMessage("Exception");
			responseBean.setStatusCode(501);
			return responseBean;
		}
	} // End of sendEmail()
}
