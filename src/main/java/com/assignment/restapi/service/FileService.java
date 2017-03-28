package com.assignment.restapi.service;

import java.io.File;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.assignment.restapi.dao.IFileDao;
import com.assignment.restapi.enums.SearchCriteriaEnum;
import com.assignment.restapi.model.FileData;
import com.assignment.restapi.model.FileMetaData;

@Service("fileService")
public class FileService implements IFileService {
	private static final Logger logger = LoggerFactory
			.getLogger(FileService.class);
	private static final String DEFAULT_ENCODING = "utf-8";

	@Autowired
	private IFileDao fileDao;

	@Autowired
	private JavaMailSender javaMailService;

	@Value("${email.ToAddress}")
	private String toEmailAddress;

	public void save(FileData fileData) {
		fileDao.save(fileData);
		// return document.getMetadata();
	}

	public List<FileMetaData> getFileMetaData(String userName) {
		return fileDao.getFileMetaData(userName);

	}

	public File getFileDataUsingFileId(String fileId) {
		return fileDao.getFileDataUsingFileId(fileId);

	}

	public void sendEmail(List<String> fileNames) {

		logger.info("**** Sending Email TO -" + toEmailAddress);
		try {
			MimeMessage msg = javaMailService.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true,
					DEFAULT_ENCODING);

			helper.setTo(toEmailAddress);
			helper.setSubject("New Files Loaded During Last Hour");

			String content = "Files Load -" + fileNames.toString();
			helper.setText(content, true);
			javaMailService.send(msg);
		} catch (MessagingException e) {
			logger.error("build email failed", e);
		} catch (Exception e) {
			logger.error("send email failed", e);
		}

	}

	/*
	 * public void sendEMail(List<String> fileNames){
	 * 
	 * 
	 * 
	 * MimeMessagePreparator preparator =
	 * getMessagePreparator("rsurve@gmail.com",fileNames.toString());
	 * 
	 * try { javaMailService.send(preparator);
	 * System.out.println("Message Send...Hurrey"); } catch (MailException ex) {
	 * System.err.println(ex.getMessage()); }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * private MimeMessagePreparator getMessagePreparator(final String
	 * emailTo,String fileNames) {
	 * 
	 * MimeMessagePreparator preparator = new MimeMessagePreparator() {
	 * 
	 * public void prepare(MimeMessage mimeMessage) throws Exception {
	 * mimeMessage.setFrom("customerserivces@yourshop.com");
	 * mimeMessage.setRecipient(Message.RecipientType.TO, new
	 * InternetAddress(emailTo));
	 * mimeMessage.setText("Files Load - "+fileNames);
	 * mimeMessage.setSubject("New Files Load During Last Hour"); } }; return
	 * preparator; }
	 */

	public List<String> searchFileIdsBasedOnCriteria(
			SearchCriteriaEnum searchCriteria, String criteriaVal) {
		return fileDao
				.searchFileIdsBasedOnCriteria(searchCriteria, criteriaVal);
	}

}
