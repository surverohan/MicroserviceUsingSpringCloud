package com.assignment.restapi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assignment.restapi.model.FileMetaData;

public class FileUtility {
	private static final Logger logger = LoggerFactory
			.getLogger(FileUtility.class);

	public static String generateUUID(byte[] data, byte[] addOnData) {

		byte[] combined = new byte[data.length + addOnData.length];

		System.arraycopy(data, 0, combined, 0, data.length);
		System.arraycopy(addOnData, 0, combined, data.length, addOnData.length);
		return UUID.nameUUIDFromBytes(combined).toString();
	}

	private static DateTimeFormatter fmt = DateTimeFormatter
			.ofPattern("yyyy-MM-dd");

	public static String getFormattedDate(LocalDate fileUploadDate) {
		return fileUploadDate.format(fmt).toString();
	}

	public static LocalDate getDateFromString(String fileUploadDate) {
		return LocalDate.parse(fileUploadDate, fmt);
	}

	public static File getFileData(String directoryName, String fileId) {

		List<String> fileToDownLoad = new ArrayList<>();
		File directory = new File(directoryName);

		List<File> files = (List<File>) FileUtils.listFiles(directory, null,
				true);
		Map<String, File> fileMap = files.stream().collect(
				Collectors.toMap(f -> f.getAbsolutePath(), f -> f));

		files.stream()
				.filter(file -> file.getAbsolutePath().contains(".properties"))
				.forEach(
						file -> {

							try {
								Properties fileMetaDataProp = readProperties(file);
								if (fileId.equals(fileMetaDataProp
										.getProperty("FileId"))) {
									fileToDownLoad.add(fileMetaDataProp
											.getProperty("FileName"));
								}
							} catch (IOException e) {
								logger.error(" File Error " + e.getMessage(), e);

							}
						});

		if (fileToDownLoad.isEmpty()) {
			return null;
		}
		Optional<File> result = fileMap.entrySet().stream()
				.filter(map -> map.getKey().contains(fileToDownLoad.get(0)))
				.map(map -> map.getValue()).findFirst();

		return result.get();
	}

	public static List<String> newFileCreated(String directoryName,
			long minLoadDuration) {

		File directory = new File(directoryName);

		List<File> files = (List<File>) FileUtils.listFiles(directory, null,
				true);
		List<String> newFileCreated = files
				.stream()
				.filter(file -> isFileCreatedWithInGivenTimeLimit(file,
						minLoadDuration)).map(file -> file.getName())
				.collect(Collectors.toList());
		return newFileCreated;

	}

	private static boolean isFileCreatedWithInGivenTimeLimit(File file,
			long minLoadDuration) {
		boolean oldFile = false;
		BasicFileAttributes attr = null;
		try {
			attr = Files.readAttributes(file.toPath(),
					BasicFileAttributes.class);

			Date now = new Date();
			Date filedate2 = new Date(attr.creationTime().toMillis());
			if ((now.getTime() - filedate2.getTime()) < minLoadDuration) {
				oldFile = true;
				logger.info("Creation date: " + attr.creationTime()
						+ "Last access date: " + attr.lastAccessTime()
						+ "Last modified date: " + attr.lastModifiedTime());

				logger.info("-- file is created within 1 hour itself"
						+ filedate2);

			}

		} catch (IOException e) {
			logger.error(" File read error! " + e.getMessage());
		}
		return oldFile;
	}

	public static List<FileMetaData> getAllFilesMetaData(String directoryName,
			String userName) {

		List<FileMetaData> fileMetaDataList = new CopyOnWriteArrayList<>();
		File directory = new File(directoryName);

		String[] extensions = new String[] { "properties" };
		List<File> files = (List<File>) FileUtils.listFiles(directory,
				extensions, true);
		files.parallelStream()
				.forEach(
						file -> {
							try {
								Properties fileMetaDataProp = readProperties(file);
								if (userName.equals(fileMetaDataProp
										.getProperty("FileUserName"))) {
									fileMetaDataList.add(new FileMetaData(
											fileMetaDataProp
													.getProperty("FileName"),
											fileMetaDataProp
													.getProperty("FileId"),
											fileMetaDataProp
													.getProperty("FileUploadedDate"),
											fileMetaDataProp
													.getProperty("FileUserName"),
											Integer.valueOf(
													fileMetaDataProp
															.getProperty("FileSize"))
													.longValue()));
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								logger.error(
										" FileMetaData Error " + e.getMessage(),
										e);

							}
						});

		return fileMetaDataList;
	}

	public static List<String> getFileIDsForCriteria(String directoryName,
			String criteriaVal, String criteriaType) {

		List<String> fileIDsList = new CopyOnWriteArrayList<>();
		File directory = new File(directoryName);

		String[] extensions = new String[] { "properties" };
		List<File> files = (List<File>) FileUtils.listFiles(directory,
				extensions, true);

		files.parallelStream().forEach(
				file -> {

					try {
						Properties fileMetaDataProp = readProperties(file);
						if (criteriaVal.equals(fileMetaDataProp
								.getProperty(criteriaType))) {
							fileIDsList.add(fileMetaDataProp
									.getProperty("FileId"));
						}
					} catch (IOException e) {
						logger.error(" File Error " + e.getMessage(), e);

					}
				});

		return fileIDsList;
	}

	private static Properties readProperties(File file) throws IOException {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			prop.load(input);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error(" Property  Error " + e.getMessage(), e);
				}
			}
		}
		return prop;
	}

}
