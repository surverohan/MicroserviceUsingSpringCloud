package com.assignment.restapi.model;

import java.time.LocalDate;
import java.util.Properties;

import com.assignment.restapi.util.FileUtility;

public class FileMetaData {

	@Override
	public String toString() {
		return "FileMetaData [fileName=" + fileName + ", fileUploadDate="
				+ fileUploadDate + ", userName=" + userName + ", fileSize="
				+ fileSize + ", fileId=" + fileId + "]";
	}

	private String fileName;
	private String fileUploadDate;
	private String userName;
	private long fileSize;
	private String fileId;

	// Introducing the dummy constructor
	public FileMetaData() {
	}

	public Properties fetchProperties() {
		if (properties == null) {
			properties = createProperties();
		}
		return properties;
	}

	private Properties properties;

	public String getFileId() {
		return fileId;
	}

	public String createFilePropertyName() {
		StringBuilder sb = new StringBuilder("/");
		sb.append(fileId).append(".properties");
		return sb.toString();
	}

	public String createFilePath() {
		StringBuilder sb = new StringBuilder("/");
		sb.append(fileId);
		return sb.toString();
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileUploadDate() {
		return fileUploadDate;
	}

	public String getUserName() {
		return userName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public FileMetaData(String fileName, String fileId, String fileUploadDate,
			String userName, long fileSize) {
		super();
		this.fileName = fileName;
		this.fileUploadDate = fileUploadDate;
		this.userName = userName;
		this.fileSize = fileSize;
		this.fileId = fileId;

	}

	private Properties createProperties() {
		Properties props = new Properties();
		props.setProperty("FileId", fileId);
		props.setProperty("FileName", fileName);
		props.setProperty("FileSize", Long.valueOf(fileSize).toString());
		props.setProperty("FileUploadedDate",
				FileUtility.getFormattedDate(LocalDate.now()));
		props.setProperty("FileUserName", userName);

		return props;
	}

}
