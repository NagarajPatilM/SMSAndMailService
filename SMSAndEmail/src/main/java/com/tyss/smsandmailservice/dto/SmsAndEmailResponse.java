package com.tyss.smsandmailservice.dto;
import java.io.Serializable;

import lombok.Data;
@SuppressWarnings("serial")
@Data

public class SmsAndEmailResponse implements Serializable{
	
	/*
	 * private int statusCode; private String message; private String description;
	 */
	
	private boolean isError;
	private DataBean data;
	private String message;
	
}
