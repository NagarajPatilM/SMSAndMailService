package com.tyss.smsandmailservice.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.tyss.smsandmailservice.dto.SmsBean;

import lombok.extern.java.Log;
@Log
@Service
public class SmsServiceImpl implements SmsService {

	@Override
	public String sendSms(SmsBean smsBean) {
		try {
			// Construct data
			String apiKey = "apikey=" + "VAsK3r/03PQ-K9YUfWPh4DGYWKF9NVjohIKXyqrkJj";
			String message = "&message=" + smsBean.getMessage();
			String sender = "&sender=" + "TXTLCL";
			String numbers = "&numbers=" + smsBean.getNumber();

			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final String string = new String();
			String line;
			while ((line = rd.readLine()) != null) {
				string.concat(line);
			}
			rd.close();
			return string.toString();
		} catch (Exception e) {
			log.info("Exception"+e);
			return "Error " + e;
		}
	}
}
