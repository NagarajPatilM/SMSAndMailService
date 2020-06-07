package com.tyss.smsandmailservice.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.tyss.smsandmailservice.dto.SmsBean;

import lombok.extern.java.Log;

/**
 * The {@code SmsServiceImpl} class provides the implementation to the abstract
 * methods od {@code SmsService} interface. In this class we are making an API
 * call to TextLocal API to send sms's
 */
@Log
@Service
public class SmsServiceImpl implements SmsService {

	/**
	 * Sends SMS to the given number with the given message
	 * 
	 * @param smsBean the bean which contains mobile number and message to which the
	 *                SMS has to be sent
	 * @return String
	 */

	@Override
	public String sendSms(SmsBean smsBean) {
		try {
			// Construct data
			String apiKey = "apikey=" + "xyz";
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
		} // End of try
		catch (Exception e) {
			log.info("Exception " + e);
			return "Error " + e;
		} // End of catch
	}// End of sendSms()
}// End of class
