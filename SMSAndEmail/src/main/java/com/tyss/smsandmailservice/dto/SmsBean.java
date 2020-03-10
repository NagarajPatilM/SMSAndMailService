package com.tyss.smsandmailservice.dto;

import java.io.Serializable;


import lombok.Data;


@Data
public class SmsBean implements Serializable {

	private String message;
	private String number;
	
	
}
