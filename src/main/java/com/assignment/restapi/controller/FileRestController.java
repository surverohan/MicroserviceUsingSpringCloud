package com.assignment.restapi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.assignment.restapi.enums.SearchCriteriaEnum;
import com.assignment.restapi.model.FileData;
import com.assignment.restapi.model.FileMetaData;
import com.assignment.restapi.service.IFileService;
import com.assignment.restapi.util.FileUtility;

@RestController
@RequestMapping(value = "/fileService")
@MultipartConfig(fileSizeThreshold = 20971520)
public class FileRestController {

	private static final Logger logger = LoggerFactory
			.getLogger(FileRestController.class);

	@Autowired
	IFileService fileService;

	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response,
			@RequestParam(value = "fileId", required = true) String fileId) {
		if (fileId == null || fileId.trim().isEmpty()) {

			throw new NoValidContent(" Invalid fileId- " + fileId);

		}

		File file = fileService.getFileDataUsingFileId(fileId);
		if (file != null && file.isFile()) {
			InputStream in;
			try {
				in = new FileInputStream(file);
				response.setContentType("txt/plain");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + file.getName());
				response.setHeader("Content-Length",
						String.valueOf(file.length()));

				FileCopyUtils.copy(in, response.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new NotFoundException("IO Issue " + e.getMessage());

			}
		} else {
			// TODO Auto-generated catch block
			throw new NotFoundException(" File not Found for fileId- " + fileId);
		}
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class NotFoundException extends RuntimeException {
		public NotFoundException(String message) {
			super(message);
		}
	}

	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public class NoValidContent extends RuntimeException {
		public NoValidContent(String message) {
			super(message);
		}
	}

	@RequestMapping(value = "/findMetaData", method = RequestMethod.GET)
	public HttpEntity<List<FileMetaData>> getFileMetaData(
			@RequestParam(value = "user", required = true) String user) {
		if (user == null || user.trim().isEmpty()) {
			return new ResponseEntity<List<FileMetaData>>(
					HttpStatus.BAD_REQUEST);
			// throw new NoValidContent("User Value is invaalid" +user);

		}

		List<FileMetaData> list = fileService.getFileMetaData(user);
		HttpHeaders headers = new HttpHeaders();
		headers.add("File Medata Count", Integer.valueOf(list.size())
				.toString());
		headers.add("Content-Type", "application/json; charset=utf-8");

		if (list.isEmpty()) {
			headers.add("message", " No File MetaData for user ");
			return new ResponseEntity<List<FileMetaData>>(HttpStatus.NO_CONTENT);

		}

		return new ResponseEntity<List<FileMetaData>>(list, headers,
				HttpStatus.OK);
	}

	@RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
	public ResponseEntity<FileMetaData> uploadFile(
			@RequestParam("file") MultipartFile file,
			@RequestParam(value = "user", required = true) String user) {
		if (file.isEmpty()) {
			return new ResponseEntity<FileMetaData>(HttpStatus.BAD_REQUEST);
		}

		// Uploading File into Server location
		try {
			FileData fileData = new FileData(file.getOriginalFilename(),
					FileUtility.getFormattedDate(LocalDate.now()), user,
					file.getSize(), file.getBytes());

			fileService.save(fileData);
			HttpHeaders headers = new HttpHeaders();
			headers.add("File Uploaded Successfully - ",
					file.getOriginalFilename());
			return new ResponseEntity<FileMetaData>(fileData.getMetadata(),
					headers, HttpStatus.OK);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			return new ResponseEntity<FileMetaData>(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/searchFileIds", method = RequestMethod.GET)
	public HttpEntity<List<String>> searchFileIdsBasedOnCriteria(
			@RequestParam(value = "criteriaType", required = true) String criteriaType,
			@RequestParam(value = "criteriaVal", required = true) String criteriaVal) {
		if (criteriaVal == null || criteriaVal.trim().isEmpty()) {
			return new ResponseEntity<List<String>>(HttpStatus.BAD_REQUEST);

		}
		if (criteriaType == null
				|| criteriaType.trim().isEmpty()
				|| !Arrays.stream(SearchCriteriaEnum.values()).anyMatch(
						(t) -> t.name().equals(criteriaType))) {
			return new ResponseEntity<List<String>>(HttpStatus.BAD_REQUEST);

		}

		List<String> list = fileService.searchFileIdsBasedOnCriteria(
				SearchCriteriaEnum.valueOf(criteriaType), criteriaVal);
		HttpHeaders headers = new HttpHeaders();
		headers.add("File Medata Count", Integer.valueOf(list.size())
				.toString());
		headers.add("Content-Type", "application/json; charset=utf-8");

		if (list.isEmpty()) {
			return new ResponseEntity<List<String>>(HttpStatus.NO_CONTENT);

		}

		return new ResponseEntity<List<String>>(list, headers, HttpStatus.OK);
	}
}
