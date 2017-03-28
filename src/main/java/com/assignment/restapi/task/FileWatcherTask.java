package com.assignment.restapi.task;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.assignment.restapi.service.IFileService;
import com.assignment.restapi.util.FileUtility;

@Component
public class FileWatcherTask {

	private static final Logger logger = LoggerFactory
			.getLogger(FileWatcherTask.class);

	@Value("${file.storageLocation}")
	private String storageLoction;

	@Value("${file.loadDurationInMilliSeconds}")
	private Integer minLoadDuration;

	@Value("${file.cronScheduleStartDuration}")
	private String cronScheduleStartDuration;

	@Autowired
	IFileService fileService;

	// @Scheduled(fixedRate = 3600000) // every 1 hr
	@Scheduled(cron = "${file.cronScheduleStartDuration}")
	// of every hour of every day.
	public void watchForFileNewlyLoaded() {
		logger.info("Invoked Poller to check for new file loaded  *************"
				+ LocalDateTime.now());
		List<String> newfileCreatedList = FileUtility.newFileCreated(
				storageLoction, minLoadDuration.longValue());
		if (!newfileCreatedList.isEmpty()) {
			fileService.sendEmail(newfileCreatedList);

		}
	}

	// examples of other CRON expressions
	// * "0 0 * * * *" = the top of every hour of every day.
	// * "*/10 * * * * *" = every ten seconds.
	// * "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
	// * "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
	// * "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
	// * "0 0 0 25 12 ?" = every Christmas Day at midnight

}
