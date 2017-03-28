package com.assignment.restapi.service;

import java.io.File;
import java.util.List;

import com.assignment.restapi.enums.SearchCriteriaEnum;
import com.assignment.restapi.model.FileData;
import com.assignment.restapi.model.FileMetaData;

public interface IFileService {

	void save(FileData fileData);

	List<FileMetaData> getFileMetaData(String userName);

	File getFileDataUsingFileId(String fileId);

	void sendEmail(List<String> fileNames);

	List<String> searchFileIdsBasedOnCriteria(
			SearchCriteriaEnum searchCriteria, String criteriaVal);

}
