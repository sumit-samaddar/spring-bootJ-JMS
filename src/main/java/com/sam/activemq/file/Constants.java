package com.sam.activemq.file;

/**
 * The constants for this demo.
 * 
 * @author Mary.Zheng
 *
 */
public class Constants {
	public static final String FILE_INPUT_DIRECTORY = "C:\\temp\\input";
	public static final String FILE_NAME = "fileName";

	public static final String FILE_OUTPUT_BYTE_DIRECTORY = "C:\\temp\\output\\bytes\\";
	public static final String FILE_OUTPUT_BLOB_DIRECTORY = "C:\\temp\\output\\blob\\";

	public static final String TEST_QUEUE = "test.queue";
	public static final String TEST_BROKER_URL = "tcp://localhost:8161";
	public static final String ADMIN = "admin";
	
	public static final String BLOB_FILESERVER = "?jms.blobTransferPolicy.defaultUploadUrl=http://localhost:8161/fileserver/";

}
