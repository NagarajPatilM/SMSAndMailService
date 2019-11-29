package com.tyss.smsandmailservice.dto;
import java.io.Serializable;

import lombok.Data;
@Data

public class Response implements Serializable{
	
	private int statusCode;
	private String message;
	private String description;
}
