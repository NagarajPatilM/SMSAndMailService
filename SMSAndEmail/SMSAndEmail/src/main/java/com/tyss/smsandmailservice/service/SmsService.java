package com.tyss.smsandmailservice.service;

import com.tyss.smsandmailservice.dto.SmsBean;

public interface SmsService {

	public String sendSms(SmsBean smsBean);

}
