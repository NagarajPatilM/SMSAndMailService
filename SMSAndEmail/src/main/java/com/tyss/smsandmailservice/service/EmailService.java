package com.tyss.smsandmailservice.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tyss.smsandmailservice.dto.SmsAndEmailResponse;

public interface EmailService {

	public SmsAndEmailResponse sendEmail(String from, String tos, String subject, String ccs ,String content, List<MultipartFile> file)throws Exception;
	
}
