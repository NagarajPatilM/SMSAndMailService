package com.tyss.smsandmailservice;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmsandemailserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsandemailserviceApplication.class, args);
	
	  }
	}


