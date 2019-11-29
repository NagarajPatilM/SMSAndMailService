package com.tyss.smsandmailservice.dto;

import java.io.Serializable;
import java.util.List;

import com.sendgrid.helpers.mail.objects.Email;

import lombok.Data;

@Data
public class EmailBean implements Serializable{
	
	private String from;
	private String message;	
	private List<Email> tos;
	private List<Email> ccs;
	
}
