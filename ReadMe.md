

• API to upload a file with a few meta-data fields. Persist meta-data in persistence store (In memory DB or file system and store the content on a file system)
Can be tested - 
SpringBootRestApi\src\test\FileUploader.html

• API to get file meta-data
Can be tested using Advance Rest Client - 
http://localhost:8080/fileService/findMetaData?user=xyxabc
http://localhost:8080/fileService/findMetaData?user=qwerty
Also RestTemplate client is approved for this functionality which can be involved as standalone process

• API to download content stream (Optional)
Can be tested using Advance Rest Client - 
http://localhost:8080/fileService/downloadFile?fileId=344105e7-141e-36b0-801f-5bb4fae89087

• API to search for file IDs with a search criterion (Optional)
Can be tested using Advance Rest Client -  - using to search type filename or username
http://localhost:8080/fileService/searchFileIds?criteriaType=USERNAME&criteriaVal=xyxabc
http://localhost:8080/fileService/searchFileIds?criteriaType=FILENAME&criteriaVal=test2.docx

• Write a scheduler in the same app to poll for new items in the last hour and send an email (Optional)
Can be tested -
To test this functionality set email.ToAddress in application.assignment.properties or application.localtest.properties

Note:Set  @PropertySource("application-localtest.properties") or  @PropertySource("application-assignment.properties") as per testing 
Since Profiler is not setup.