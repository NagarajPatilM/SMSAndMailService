package com.tyss.smsandmailservice.dto;
import java.io.Serializable;

import lombok.Data;
@SuppressWarnings("serial")
@Data

public class SmsAndEmailResponse implements Serializable{
	

	
	private boolean error;
	private String message;
	
}
