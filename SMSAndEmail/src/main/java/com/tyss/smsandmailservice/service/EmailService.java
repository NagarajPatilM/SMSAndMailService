package com.tyss.smsandmailservice.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sendgrid.helpers.mail.objects.Email;
import com.tyss.smsandmailservice.dto.Response;

public interface EmailService {

	public Response sendEmail2(String from, String tos, String subject, String ccs ,String content, List<MultipartFile> file)throws Exception;
	
}
