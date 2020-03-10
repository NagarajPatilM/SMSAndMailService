package com.tyss.smsandmailservice.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class EmailBean implements Serializable{

	private String from;
	private String subject;
	private List<Email> tos;
	private List<Email> ccs;
	private String content;


}
