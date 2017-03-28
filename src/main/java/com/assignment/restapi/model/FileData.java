package com.assignment.restapi.model;

import com.assignment.restapi.util.FileUtility;

public class FileData extends FileMetaData {
	public FileData(String fileName, String fileUploadDate, String userName,
			long fileSize, byte[] fileData) {
		// ********** replace UUID with other valid alogo for thread safely
		super(fileName,
				FileUtility.generateUUID(fileData, userName.getBytes()),
				fileUploadDate, userName, fileSize);
		this.fileData = fileData;
	}

	private byte[] fileData;

	public byte[] getFileData() {
		return fileData;
	}

	public FileMetaData getMetadata() {
		return new FileMetaData(getFileName(), getFileId(),
				getFileUploadDate(), getUserName(), getFileSize());
	}

	public FileData() {
		super();
	}

}
