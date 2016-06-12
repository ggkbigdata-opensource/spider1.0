package com.ggk.spider.datasource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tomcat.datasource",
                         ignoreUnknownFields = false)
public class DataSourceProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int maxActive;
    private int maxIdle;
    private int minIdle;
    private int initialSize;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean testWhileIdle;
    private boolean testOnConnect;
    private String validationQuery;
    private boolean logValidationErrors;
    private int timeBetweenEvictionRunsMillis;
    
    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
    	//String key = "xgsdkNiuX";
    	String fieldName = "tomcat.datasource.password";
    	String realPassword = password;
    	try {
    		//如果能解密，说明已经加密
    		realPassword = password;
    	} catch (Throwable t) {
    		//如果不能解密，说明没有加密
    		realPassword = password;
    		BufferedReader reader = null;
    		BufferedWriter writer = null;
    		try {
				//String filePassword = AesUtil.encrypt(realPassword, key);
    			String filePassword = realPassword;
				File applicationFile = new File("application.properties");
				if (!applicationFile.exists()) {
					applicationFile = new File("./config/application.properties");
				}
				if (applicationFile.exists()) {
					reader = new BufferedReader(new FileReader(applicationFile));
					String readLine = reader.readLine();
					StringBuilder builder = new StringBuilder();
					while (readLine != null) {
						if (readLine.startsWith(fieldName)) {
							builder.append(fieldName).append("=").append(filePassword);
						} else {
							builder.append(readLine);
						}
						builder.append("\n");
						readLine = reader.readLine();
					}
					writer = new BufferedWriter(new FileWriter(applicationFile));
					writer.write(builder.toString());
				}
        	} catch (Throwable t1) {
        		t1.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (writer != null) {
					try {
						writer.flush();
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
    		
    	}
        this.password = realPassword; 
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnConnect() {
        return testOnConnect;
    }

    public void setTestOnConnect(boolean testOnConnect) {
        this.testOnConnect = testOnConnect;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public boolean isLogValidationErrors() {
        return logValidationErrors;
    }

    public void setLogValidationErrors(boolean logValidationErrors) {
        this.logValidationErrors = logValidationErrors;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }
}