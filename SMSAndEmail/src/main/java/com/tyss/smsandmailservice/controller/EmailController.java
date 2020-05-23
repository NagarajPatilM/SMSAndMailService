package com.tyss.smsandmailservice.controller;

import java.util.List;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tyss.smsandmailservice.dto.SmsAndEmailResponse;
import com.tyss.smsandmailservice.service.EmailService;

import lombok.extern.java.Log;

/**
 * 
 * The class contains methods which handles the requests related to Email coming
 * from the Browser and interacts with the service layer for the Response
 *
 */
@Log
@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
@MultipartConfig(maxFileSize = 1024 * 1024 * 1024, maxRequestSize = 1024 * 1024 * 1024)
public class EmailController {

	@Autowired
	private EmailService service;

	@PostMapping(value = "/send-email", produces = MediaType.APPLICATION_JSON_VALUE, consumes = { "multipart/form-data",
			"application/json","application/x-www-form-urlencoded;charset=UTF-8" })
	public SmsAndEmailResponse sendEmail(@RequestHeader String from, @RequestHeader String subject,
			@RequestHeader String tos, @RequestHeader String ccs, @RequestHeader String content,
			@RequestBody List<MultipartFile> file) throws Exception {
		log.info("file : " + file);
		return service.sendEmail(from, tos, subject, ccs, content, file);
	}

}
