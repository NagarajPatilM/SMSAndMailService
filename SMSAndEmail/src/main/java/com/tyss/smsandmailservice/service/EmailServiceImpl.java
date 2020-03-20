package com.tyss.smsandmailservice.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.tyss.smsandmailservice.dto.SmsAndEmailResponse;

import lombok.extern.java.Log;

@Log
@Service
public class EmailServiceImpl implements EmailService {

	public com.tyss.smsandmailservice.dto.SmsAndEmailResponse sendEmail(String from, String tos, String subject,
			String ccs, String content, List<MultipartFile> file) {

		SmsAndEmailResponse responseBean = new SmsAndEmailResponse();
		Mail mail = new Mail();
		FileOutputStream fos = null;
		FileInputStream fis = null;
		FileReader fileReader = null;

		try {
			ArrayList<String> to = jsonStringToArray(tos); // converting Stringified JSON to ArrayList<String>.
			ArrayList<String> cc = jsonStringToArray(ccs);

			Personalization personalization = new Personalization();

			Email fro = new Email(from);

			Content cont = new Content("text/plain", content);

			mail.setSubject(subject);
			mail.setFrom(fro);
			mail.addContent(cont);
			personalization.addTo(new Email(to.get(0)));

			mail.addPersonalization(personalization);

			for (int j = 1; j < to.size(); j++) {
				if (to.get(j) != null) {
					mail.personalization.get(0).addTo(new Email(to.get(j)));
				}
			}

			for (int k = 0; k < cc.size(); k++) {
				if (cc.get(k) != null) {
					mail.personalization.get(0).addCc(new Email(cc.get(k)));
				}
			}

			/*
			 * for loop iterates for no. of attachments sent
			 */

			for (int i = 0; i < file.size(); i++) {

				String fileName = file.get(i).getOriginalFilename();
				String[] ext = fileName.split("\\.");

				if (ext[1].equalsIgnoreCase("txt")) {

					// Attachment for text file goes here

					File convFile = new File(file.get(i).getOriginalFilename());
					fos = new FileOutputStream(convFile);
					fos.write(file.get(i).getBytes());
					fos.close();

					byte[] fileData = new byte[(int) convFile.length()];

					fis = new FileInputStream(convFile);
					fis.read(fileData); // read file into bytes[]
					fis.close();

					Attachments attachement = new Attachments();
					String con = new String(fileData, 0, (int) convFile.length(), "UTF-8");
					String attachementContent = java.util.Base64.getMimeEncoder().encodeToString(con.getBytes());
					attachement.setContent(attachementContent);
					attachement.setType("application/text");
					attachement.setFilename(file.get(i).getOriginalFilename());
					attachement.setDisposition("attachment");
					attachement.setContentId("Banner");
					mail.addAttachments(attachement); // End of attachments 1

				} else if (ext[1].equals("pdf")) {

					// Attachment for pdf file goes here below

					File convFile1 = new File(file.get(i).getOriginalFilename());

					byte[] c = file.get(i).getBytes();

					byte[] encodedBytes = Base64.getEncoder().encode(c);
					String pngInBase64 = new String(encodedBytes, 0, encodedBytes.length, "UTF-8");
					String pngContent = new String(c, 0, c.length, "UTF-8");
					fos = new FileOutputStream(convFile1);
					fos.write(file.get(i).getBytes());
					PDDocument document = PDDocument.load(convFile1);

					// Instantiate PDFTextStripper class
					PDFTextStripper pdfStripper = new PDFTextStripper();

					// Retrieving text from PDF document
					String text = pdfStripper.getText(document);
					// Closing the document
					document.close();

					byte[] c1 = text.getBytes();

					byte[] encodedBytes1 = java.util.Base64.getEncoder().encode(c);
					String pdfInBase64 = new String(encodedBytes, 0, encodedBytes.length, "UTF-8");

					String pdfContent = new String(c, 0, c.length, "UTF-8");

					Attachments attachment1 = new Attachments();
					attachment1.setContent(pdfInBase64);
					attachment1.setFilename(file.get(i).getOriginalFilename());
					attachment1.setDisposition("attachment");
					attachment1.setContentId("Balance Sheet");
					attachment1.setType("application/pdf");
					mail.addAttachments(attachment1);

				} else if (ext[1].equalsIgnoreCase("png")) {

					// Attachment for png file goes here below

					byte[] c = file.get(i).getBytes();
					byte[] encodedBytes = Base64.getEncoder().encode(c);
					String pngInBase64 = new String(encodedBytes, 0, encodedBytes.length, "UTF-8");
					log.info(pngInBase64);
					String pngContent = new String(c, 0, c.length, "UTF-8");
					log.info("\n" + pngContent);

					Attachments attachment2 = new Attachments();
					attachment2.setContent(pngInBase64);
					attachment2.setFilename(file.get(i).getOriginalFilename());
					attachment2.setDisposition("attachment");
					attachment2.setContentId("Banner");
					attachment2.setType("image/png");
					mail.addAttachments(attachment2);
				}
			} // End of for loop
			fileReader = new FileReader(
					"C:\\new-eclipse-workspace\\SMSAndMailService\\SMSAndEmail\\src\\main\\java\\attachements.properties");
			Properties properties = new Properties();
			properties.load(fileReader);
			SendGrid sg = new SendGrid(properties.getProperty("ApiKey"));
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			responseBean.setError(false);
			responseBean.setMessage("Success");
			return responseBean;
		} // End of try
		catch (Exception e) {
			log.info("Exception message" + e);
			responseBean.setMessage("Exception");
			responseBean.setError(true);
			return responseBean;
		} // End of catch
		finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (fileReader != null) {
					fileReader.close();
				}
			} catch (IOException e) {
				log.info("Exception occured " + e);
			}
		} // End of finally
	} // End of sendEmail()

	// Method for converting String to ArrayList<String>
	ArrayList<String> jsonStringToArray(String tos) {
		ArrayList<String> stringArray = new ArrayList<>();
		JSONArray jsonArray = new JSONArray(tos);
		for (int i = 0; i < jsonArray.length(); i++) {
			stringArray.add(jsonArray.getString(i));
		}
		return stringArray;
	} // End of jsonStringToArray( )

}
