package com.tyss.smsandmailservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sendgrid.helpers.mail.objects.Email;
import com.tyss.smsandmailservice.dto.EmailBean;
import com.tyss.smsandmailservice.dto.Response;
import com.tyss.smsandmailservice.service.EmailService;

@RestController
@CrossOrigin(origins = "*")
public class EmailController {

	@Autowired
	private EmailService service;

	@PostMapping(value = "/send-email", produces = MediaType.APPLICATION_JSON_VALUE)
	public Response sendEmail(@RequestBody EmailBean emailBean) {
		return service.sendEmail(emailBean.getFrom(), emailBean.getTos(), emailBean.getMessage(),  emailBean.getCcs());
	}
}
