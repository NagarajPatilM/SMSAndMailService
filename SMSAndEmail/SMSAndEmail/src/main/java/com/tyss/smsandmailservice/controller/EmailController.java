package com.tyss.smsandmailservice.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tyss.smsandmailservice.dto.EmailBean;
import com.tyss.smsandmailservice.dto.Response;
import com.tyss.smsandmailservice.service.EmailService;


 /**
  * 
  * The class contains methods which handles the 
  * requests related to Email coming from the Browser and interacts 
  * with the service layer for the Response
  *
  */
@RestController
@CrossOrigin(origins = "*")
public class EmailController {

	@Autowired
	private EmailService service;

	@RequestMapping(value = "send-email2",method = RequestMethod.POST,consumes = {"multipart/form-data","application/json"})
	//@PostMapping(value = "/send-email", produces = MediaType.APPLICATION_JSON_VALUE)
	public Response sendEmail(@RequestBody EmailBean emailBean) throws IOException {
	//	System.out.println(at);
		return service.sendEmail2(emailBean.getFrom(), emailBean.getTos(), emailBean.getSubject(), emailBean.getCcs(),emailBean.getContent(),emailBean.getAttachements());
		//return service.sendEmail(emailBean.getFrom(), emailBean.getTos(), emailBean.getSubject(), emailBean.getCcs(),emailBean.getContent());
	}
	
	
	@RequestMapping(value = "/sendgridEmail", method = RequestMethod.POST, consumes = {"multipart/form-data","application/json"})
	public String sendGridEmail(@RequestBody MultipartFile file) {
	System.out.println(file);
	return "a";
	}
}
