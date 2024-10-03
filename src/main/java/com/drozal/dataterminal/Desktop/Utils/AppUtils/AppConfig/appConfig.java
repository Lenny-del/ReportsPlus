package com.drozal.dataterminal.Desktop.Utils.AppUtils.AppConfig;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.Misc.LogUtils;

import java.io.*;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Objects;
import java.util.Properties;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;

public class appConfig {
	
	static String appConfigFilePath = getJarPath() + File.separator + "data" + File.separator + "app.properties";
	
	public static void createAppConfig() {
		File appConfigFile = new File(appConfigFilePath);
		if (appConfigFile.exists()) {
			log("app config exists, printing values", LogUtils.Severity.INFO);
		} else {
			try {
				appConfigFile.createNewFile();
				log("App Config: " + appConfigFile.getAbsolutePath(), LogUtils.Severity.INFO);
			} catch (IOException e) {
				logError("Failed to create app config file", e);
			}
		}
	}
	
	public static Double appConfigRead(String database, String property) {
		Properties prop = new Properties();
		ProtectionDomain protectionDomain = appConfig.class.getProtectionDomain();
		CodeSource codeSource = protectionDomain.getCodeSource();
		if (codeSource != null) {
			try (InputStream input = new FileInputStream(appConfigFilePath)) {
				
				prop.load(input);
				
				return Double.valueOf(prop.getProperty(database + "." + property));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			log("Unable to determine the location of the JAR file ", LogUtils.Severity.ERROR);
			return null;
		}
	}
	
	public static boolean doesAppConfigExist() {
		File configFile = new File(appConfigFilePath);
		return configFile.exists();
	}
	
	public static void checkAndSetDefaultAppValue(String newDatabase, String property, String defaultValue) {
		try {
			Properties prop = new Properties();
			ProtectionDomain protectionDomain = ConfigReader.class.getProtectionDomain();
			CodeSource codeSource = protectionDomain.getCodeSource();
			if (codeSource != null) {
				
				try (InputStream input = new FileInputStream(appConfigFilePath)) {
					prop.load(input);
				}
				
				String oldKey = "database." + property;
				if (prop.containsKey(oldKey)) {
					String value = prop.getProperty(oldKey);
					prop.remove(oldKey);
					prop.setProperty(newDatabase + "." + property, value);
				} else if (!prop.containsKey(newDatabase + "." + property)) {
					prop.setProperty(newDatabase + "." + property, defaultValue);
				}
				
				try (OutputStream output = new FileOutputStream(appConfigFilePath)) {
					prop.store(output, null);
				}
				log("Loaded " + newDatabase + " '" + property + "' with value: " + prop.getProperty(
						newDatabase + "." + property), LogUtils.Severity.DEBUG);
			} else {
				log("Unable to determine the location of the JAR file ", LogUtils.Severity.ERROR);
			}
		} catch (IOException e) {
			log("Error reading or writing app.properties file ", LogUtils.Severity.ERROR);
		}
	}
	
	public static void checkAndSetDefaultAppValues() {
		log("====================== App Config ======================", LogUtils.Severity.INFO);
		// X 45
		checkAndSetDefaultAppValue("Notes", "x", "45.0");
		checkAndSetDefaultAppValue("Notes", "y", "100.0");
		
		checkAndSetDefaultAppValue("Log Browser", "x", "45.0");
		checkAndSetDefaultAppValue("Log Browser", "y", "203.0");
		
		checkAndSetDefaultAppValue("Callouts", "x", "45.0");
		checkAndSetDefaultAppValue("Callouts", "y", "0.0");
		
		checkAndSetDefaultAppValue("CourtCase", "x", "45.0");
		checkAndSetDefaultAppValue("CourtCase", "y", "310.0");
		
		checkAndSetDefaultAppValue("Show IDs", "x", "45.0");
		checkAndSetDefaultAppValue("Show IDs", "y", "417.0");
		
		// X 200
		checkAndSetDefaultAppValue("Ped Lookup", "x", "200.0");
		checkAndSetDefaultAppValue("Ped Lookup", "y", "0.0");
		
		checkAndSetDefaultAppValue("Veh Lookup", "x", "200.0");
		checkAndSetDefaultAppValue("Veh Lookup", "y", "101.0");
		
		// X 376
		checkAndSetDefaultAppValue("Server", "x", "376.0");
		checkAndSetDefaultAppValue("Server", "y", "0.0");
		
		checkAndSetDefaultAppValue("Updates", "x", "376.0");
		checkAndSetDefaultAppValue("Updates", "y", "100.0");
		
		checkAndSetDefaultAppValue("Settings", "x", "376.0");
		checkAndSetDefaultAppValue("Settings", "y", "203.0");
		
		log("=========================================================", LogUtils.Severity.INFO);
	}
	
	public static void appConfigWrite(String database, String property, String value) {
		Properties prop = new Properties();
		OutputStream output = null;
		FileInputStream input = null;
		
		try {
			input = new FileInputStream(appConfigFilePath);
			prop.load(input);
		} catch (IOException e) {
			logError("ConfigWrite Error Code 1 ", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logError("ConfigWrite Error Code 2 ", e);
				}
			}
		}
		
		try {
			prop.setProperty(database + "." + property, value);
			output = new FileOutputStream(Objects.requireNonNull(appConfigFilePath));
			prop.store(output, null);
		} catch (IOException e) {
			logError("ConfigWrite Error Code 3 ", e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					logError("ConfigWrite Error Code 4 ", e);
				}
			}
		}
	}
	
}
