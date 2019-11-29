package com.tyss.smsandmailservice.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tyss.smsandmailservice.dto.SmsBean;
import com.tyss.smsandmailservice.service.SmsService;

@RestController
@CrossOrigin(origins = "*")
public class SmsController {
	
	@Autowired
	private SmsService service;
	
	@PostMapping(value = "/send-sms",produces = MediaType.APPLICATION_JSON_VALUE)
	public String sendSms(@RequestBody SmsBean smsBean) {
		return service.sendSms(smsBean);
	}

}
