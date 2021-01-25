package com.examstack.management.ueditor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.io.File;

public class PropertiesLoader {
	private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);
	private static final String DEFAULT_FILENAME = "default.properties";
	private static Properties properties = new Properties();

	static {

		InputStream in = PropertiesLoader.class.getResourceAsStream(DEFAULT_FILENAME);
		System.out.println("in ========= " + in);
		if (in == null) {
			logger.error("{} not found", DEFAULT_FILENAME);
			throw new RuntimeException(DEFAULT_FILENAME + " not found");
		} else {
			if (!(in instanceof BufferedInputStream))
				in = new BufferedInputStream(in);

			try {
				properties.load(in);
				in.close();
				logger.debug("{} loaded", DEFAULT_FILENAME);
			} catch (Exception e) {
				logger.error("Error while processing {}", DEFAULT_FILENAME);
				throw new RuntimeException("Error while processing " + DEFAULT_FILENAME, e);
			}
		}
	}

	public static String getProperty(final String key) {
		return properties.getProperty(key);
	}

	public static void setProperty(final String key, final String value) {
		properties.setProperty(key, value);
	}

	public static String getFileResourceTypeAllowedExtensions() {
		return properties.getProperty("resourceType.file.extensions.allowed");
	}

	public static String getImageResourceTypeAllowedExtensions() {
		return properties.getProperty("resourceType.image.extensions.allowed");
	}

	public static String getMediaResourceTypeAllowedExtensions() {
		return properties.getProperty("resourceType.media.extensions.allowed");
	}


}
