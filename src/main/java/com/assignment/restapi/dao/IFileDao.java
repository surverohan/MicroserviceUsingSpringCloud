package com.assignment.restapi.dao;


import java.io.File;
import java.util.List;

import com.assignment.restapi.enums.SearchCriteriaEnum;
import com.assignment.restapi.model.FileData;
import com.assignment.restapi.model.FileMetaData;

public interface IFileDao {
	/**
     * Inserts a file in the data store.
     * 
     * @param fileData  FileData
     */
    void save(FileData fileData);
    
    List<FileMetaData> getFileMetaData(String userName);

    File getFileDataUsingFileId(String fileId);
    
    List<String> searchFileIdsBasedOnCriteria(SearchCriteriaEnum searchCriteria,String criteriaVal);


}
