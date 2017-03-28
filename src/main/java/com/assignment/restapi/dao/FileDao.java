package com.assignment.restapi.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.assignment.restapi.enums.SearchCriteriaEnum;
import com.assignment.restapi.model.FileData;
import com.assignment.restapi.model.FileMetaData;
import com.assignment.restapi.util.FileUtility;

@Repository("fileDao")
public class FileDao implements IFileDao {
	private static final Logger logger = LoggerFactory.getLogger(FileDao.class);

	@Value("${file.storageLocation}")
	private String storageLocation;

	public void save(FileData fileData) {
		try {
			String filePath = createDirectory(fileData.createFilePath());
			saveData(fileData, filePath);
		} catch (IOException e) {
			String message = "Error while inserting document";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}

	}

	public List<FileMetaData> getFileMetaData(String userName) {
		return FileUtility.getAllFilesMetaData(storageLocation, userName);
	}

	private void saveData(FileData fileData, String filePath)
			throws IOException {

		StringBuilder fileFullName = new StringBuilder(filePath);
		fileFullName.append("/").append(fileData.getFileName());
		Files.write(Paths.get(fileFullName.toString()), fileData.getFileData());

		StringBuilder propertyFileFullName = new StringBuilder(filePath);
		propertyFileFullName.append(fileData.createFilePropertyName());
		Properties props = fileData.fetchProperties();

		File propertyFile = new File(propertyFileFullName.toString());
		OutputStream out = new FileOutputStream(propertyFile);
		props.store(out, "File Creation Addition Information");

	}

	private String createDirectory(String filePath) {
		StringBuilder sb = new StringBuilder(storageLocation);
		sb.append(filePath);
		makeDirectory(sb.toString());
		return sb.toString();
	}

	private void makeDirectory(String pathVal) {

		File file = new File(pathVal);
		file.mkdirs();
	}

	public File getFileDataUsingFileId(String fileId) {

		return FileUtility.getFileData(storageLocation, fileId);

	}

	public List<String> searchFileIdsBasedOnCriteria(
			SearchCriteriaEnum searchCriteria, String criteriaVal) {

		return FileUtility.getFileIDsForCriteria(storageLocation, criteriaVal,
				searchCriteria.getCriteriaField());

	}

}
