package com.tyss.smsandmailservice.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

//import java.util.Base64;
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

import lombok.extern.java.Log;

@Log
@Service
public class EmailServiceImpl implements EmailService {

	@Override
	public com.tyss.smsandmailservice.dto.Response sendEmail(String from, List<Email> tos, String subject,
			List<Email> ccs, String content) throws IOException {

		com.tyss.smsandmailservice.dto.Response responseBean = new com.tyss.smsandmailservice.dto.Response();
		Personalization personalization = new Personalization();

		Email fro = new Email(from);

		Content cont = new Content("text/plain", content);
		// Mail mail = new Mail(fro, subject, t, content);
		Mail mail = new Mail();
		mail.setSubject(subject);
		mail.setFrom(fro);
		personalization.addTo(tos.get(0));
		mail.addContent(cont);
		mail.addPersonalization(personalization);

		Attachments attachments1 = new Attachments();
		Path file1 = Paths.get("D:\\New Text Document.txt");
		byte[] attachmentContentBytes1 = Files.readAllBytes(file1);
		String attachementContent1 = Base64.getEncoder().encodeToString(attachmentContentBytes1);
		attachments1.setContent(attachementContent1);
		attachments1.setFilename("newfile.txt");
		attachments1.setDisposition("inline");
		attachments1.setContentId("Banner");
		attachments1.setType("application/pdf");
		mail.addAttachments(attachments1);

		Attachments attachments2 = new Attachments();
		Path file2 = Paths.get("D:\\new.txt");
		byte[] attachmentContentBytes2 = Files.readAllBytes(file2);
		String attachementContent2 = Base64.getEncoder().encodeToString(attachmentContentBytes2);
		attachments2.setContent(attachementContent2);
		attachments2.setFilename("balance.txt");
		attachments2.setDisposition("inline");
		attachments2.setContentId("Banner");
		attachments2.setType("application/pdf");
		mail.addAttachments(attachments2);

		for (int i = 1; i < tos.size(); i++) {
			if (tos.get(i) != null) {
				mail.personalization.get(0).addTo(tos.get(i));
			}
		}

		for (int i = 0; i < ccs.size(); i++) {
			if (ccs.get(i) != null) {
				mail.personalization.get(0).addCc(ccs.get(i));
			}
		}

		SendGrid sg = new SendGrid("");
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			responseBean.setStatusCode(201);
			responseBean.setMessage("Success");
			return responseBean;
		} catch (IOException ex) {
			log.info("Exception message" + ex);
			responseBean.setMessage("Exception");
			responseBean.setStatusCode(501);
			return responseBean;
		}
	} // End of sendEmail()
	

	public com.tyss.smsandmailservice.dto.Response sendEmail2(String from, String tos, String subject, String ccs,
			String content, List<MultipartFile> file) throws Exception {

		Attachments attachement1 = null;
		Attachments attachement2 = null;
		Attachments attachement3 = null;
		Attachments attachement4 = null;
		Attachments attachement5 = null;
		Attachments[] arr = { attachement1, attachement2, attachement3, attachement4, attachement5 };
		com.tyss.smsandmailservice.dto.Response responseBean = new com.tyss.smsandmailservice.dto.Response();
		Mail mail = new Mail();
		FileOutputStream fos = null;
		FileInputStream fis = null;

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

			for (int i = 0; i < file.size(); i++) {

				String fileName = file.get(i).getOriginalFilename();
				String[] ext = fileName.split("\\.");
				for (int k = 0; k < ext.length; k++) {
					log.info(ext[k]);
				}
				if (ext[1].equalsIgnoreCase("txt")) {
					/**
		        	 * Attachement for txt file goes here
					 */

					File convFile = new File(file.get(i).getOriginalFilename());
					fos = new FileOutputStream(convFile);
					fos.write(file.get(i).getBytes());
					fos.close();

					byte[] fileData = new byte[(int) convFile.length()];

					fis = new FileInputStream(convFile);
					fis.read(fileData); // read file into bytes[]
					fis.close();

					Attachments attachement = arr[i];
					attachement = new Attachments();
					String con = new String(fileData, 0, (int) convFile.length(), "UTF-8");
					String attachementContent = java.util.Base64.getMimeEncoder().encodeToString(con.getBytes());
					attachement.setContent(attachementContent);
					attachement.setType("application/text");
					attachement.setFilename(file.get(i).getOriginalFilename());
					attachement.setDisposition("attachment");
					attachement.setContentId("Banner");
					mail.addAttachments(attachement); // End of attachement 1
				} else if (ext[1].equals("pdf")) {

					/**
					 * Attachement for pdf file goes here below
					 */

					final boolean IS_CHUNKED = true;

					File convFile1 = new File(file.get(i).getOriginalFilename());
					
					byte[] c = file.get(i).getBytes();
					// byte[] fileData1 = new byte[(int) convFile1.length()];

					byte[] encodedBytes = Base64.getEncoder().encode(c);
					// byte[] encodedBytes= encode( c,IS_CHUNKED);
					String pngInBase64 = new String(encodedBytes, 0, encodedBytes.length, "UTF-8");
					//log.info(pngInBase64);
					String pngContent = new String(c, 0, c.length, "UTF-8");
					//log.info("\n"+pngContent);
					
					FileOutputStream fos1 = new FileOutputStream(convFile1);
					fos1.write(file.get(i).getBytes());
					PDDocument document = PDDocument.load(convFile1);

					// Instantiate PDFTextStripper class
					PDFTextStripper pdfStripper = new PDFTextStripper();

					// Retrieving text from PDF document
					String text = pdfStripper.getText(document);
					//log.info(" " + text);
					// Closing the document
					document.close();

					byte[] c1 = text.getBytes();
					// byte[] fileData1 = new byte[(int) convFile1.length()];

					byte[] encodedBytes1 = java.util.Base64.getEncoder().encode(c);
					// byte[] encodedBytes= encode( c,IS_CHUNKED);
					String pdfInBase64 = new String(encodedBytes, 0, encodedBytes.length, "UTF-8");

					String pdfContent = new String(c, 0, c.length, "UTF-8");

//				 byte[] encoded= encode( fileData1,IS_CHUNKED);
// 				 String pdfInBase64 = new String(encoded);
					Attachments attachments2 = new Attachments();
					attachments2.setContent(pdfInBase64);
					attachments2.setFilename(file.get(i).getOriginalFilename());
					attachments2.setDisposition("attachment");
					attachments2.setContentId("Balance Sheet");
					attachments2.setType("application/pdf");
					mail.addAttachments(attachments2);
				} else if(ext[1].equalsIgnoreCase("png")) {

					/**
					 * Attachement for png file goes here below
					 */

					byte[] c = file.get(i).getBytes();
					byte[] encodedBytes = Base64.getEncoder().encode(c);
					String pngInBase64 = new String(encodedBytes, 0, encodedBytes.length, "UTF-8");
					log.info(pngInBase64);
					String pngContent = new String(c, 0, c.length, "UTF-8");
					log.info("\n"+pngContent);

					Attachments attachments2 = new Attachments();
					attachments2.setContent(pngInBase64);
					attachments2.setFilename(file.get(i).getOriginalFilename());
					attachments2.setDisposition("attachment");
					attachments2.setContentId("Banner");
					attachments2.setType("image/png");
					mail.addAttachments(attachments2);
				}
			} // End of for loop
			SendGrid sg = new SendGrid("");
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			responseBean.setStatusCode(201);
			responseBean.setMessage("Success");
			log.info(" " + responseBean.getStatusCode());
			return responseBean;
		} // End of try
		catch (Exception e) {
			log.info("Exception message" + e);
			responseBean.setMessage("Exception");
			responseBean.setStatusCode(501);
			return responseBean;
		} // End of catch
		finally {
			if (fos != null) {
				fos.close();
			}
			if (fis != null) {
				fis.close();
			}
		} // End of finally
	} // End of sendEmail2()

	// Method for converting String to ArrayList<String>
	ArrayList<String> jsonStringToArray(String tos) {
		ArrayList<String> stringArray = new ArrayList<>();
		JSONArray jsonArray = new JSONArray(tos);
		for (int i = 0; i < jsonArray.length(); i++) {
			stringArray.add(jsonArray.getString(i));
		}
		return stringArray;
	} // End of jsonStringToArray( )

	private static byte[] encode(byte[] a, boolean IS_CHUNKED) throws Exception {
		return Base64.getEncoder().encode(a);
		// writeByteArraysToFile(destination, base64EncodedData);
	}

	@Override
	public com.tyss.smsandmailservice.dto.Response sendEmail3(String from, List<Email> tos, String subject,
			List<Email> ccs, String content, List<MultipartFile> file) throws IOException {
		return null;
	}
}
