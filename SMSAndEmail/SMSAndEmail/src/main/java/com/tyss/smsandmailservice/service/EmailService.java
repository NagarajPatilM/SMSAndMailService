package com.tyss.smsandmailservice.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sendgrid.helpers.mail.objects.Email;
import com.tyss.smsandmailservice.dto.Response;

public interface EmailService {

	public Response sendEmail(String from, List<Email> tos, String subject, List<Email> ccs ,String content) throws IOException;

	public Response sendEmail2(String from, List<Email> tos, String subject, List<Email> ccs ,String content,MultipartFile attachements)throws IOException;
}
