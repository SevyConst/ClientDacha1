package com.company;

import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ForProperties {
    public static final String ARG_START = "--pathConfig=";

    private static final String PROPERTY_IP_SERVER_1 = "ip_server_1";
    private static final String PROPERTY_PORT_DO_SERVER_1 = "port_server_1";
    private static final String PROPERTY_URL_DB = "url_db";
    private static final String PROPERTY_IS_RASPBERRY_PI = "is_raspberry_pi";
    private static final String PROPERTY_DEVICE_ID = "device_id";
    private static final String PROPERTY_PERIOD_SEC = "period";
    private static final String PROPERTY_MAX_ROWS_DB = "max_rows";
    private static final String PROPERTY_SLEEP_REMOVING = "sleep_seconds_removing";

    private String ip;
    private int port;
    private String urlDb;
    private Boolean isRpi;  // Is this client working on Raspberry Pi
    private int deviceId;
    private int period;  // Period sending data (in seconds)

    private long maxRows;  // For removing
    private static final long DEFAULT_MAX_ROWS = 60*60*24*90;  // 90 days

    private long sleepSecondsRemoving;
    private static final int DEFAULT_SLEEP_REMOVING = 60*60*24;


    boolean load(String arg, Logger logger) {
        Properties prop = new Properties();

        String path = arg.substring(ARG_START.length());

        try(InputStream inputStream = new FileInputStream(path))  {
            prop.load(inputStream);

            if (!readServerParams(prop, logger)) {
                return false;
            }

            if (!readParamsForDb(prop, logger)) {
                return false;
            }

            // Is this client working on Raspberry Pi
            Boolean isRpi = readIsRpi(prop.getProperty(PROPERTY_IS_RASPBERRY_PI), logger);
            if (null == isRpi) {
                return false;
            }
            logger.info("is raspberry pi: " + isRpi);

            // 5. Device id
            try {
                deviceId = Integer.parseInt(prop.getProperty(PROPERTY_DEVICE_ID));
            } catch (NumberFormatException e) {
                logger.error("can't parse properties: can't read device_id", e);
                return false;
            }
            logger.info("Device id: " + deviceId);

            // 6. Period sending data (in seconds)
            try {
                period = Integer.parseInt(prop.getProperty(PROPERTY_PERIOD_SEC));
            } catch (NumberFormatException e) {
                logger.error("can't parse properties: can't read period sending data", e);
                return false;
            }
            logger.info("Period sending data: " + period + " seconds");

        } catch(IOException e){
            logger.error("can't read properties", e);
            return false;
        }

        return true;
    }

    private boolean readServerParams(Properties prop, Logger logger) {

        // ip
        ip = prop.getProperty(PROPERTY_IP_SERVER_1);
        if (null == ip) {
            logger.error("null == ip");
            return false;
        }
        logger.info("Server IP: " + ip);

        // port
        try {
            port = Integer.parseInt(prop.getProperty(PROPERTY_PORT_DO_SERVER_1));
        } catch (NumberFormatException e) {
            logger.error("can't parse properties: can't read port", e);
            return false;
        }
        logger.info("Server Port: " + port);

        return true;
    }

    private boolean readParamsForDb(Properties prop, Logger logger) {

        urlDb = prop.getProperty(PROPERTY_URL_DB);
        if (null == urlDb) {
            logger.error("can't parse url for Db");
            return false;
        }
        logger.info("Url for SQlite: " + urlDb);

        try {
            maxRows = Long.parseLong(prop.getProperty(PROPERTY_MAX_ROWS_DB));
        } catch (NumberFormatException e) {
            logger.warn("can't parse properties: can't read max rows. Using default value: "
                    + DEFAULT_MAX_ROWS + " rows", e);
            maxRows = DEFAULT_MAX_ROWS;
        }
        logger.info("max rows: " + maxRows);

        try {
            sleepSecondsRemoving = Long.parseLong(prop.getProperty(PROPERTY_SLEEP_REMOVING));
        } catch (NumberFormatException e) {
            logger.warn(
                    "can't parse properties: can't read sleep removing. Using default value: " +
                            DEFAULT_SLEEP_REMOVING + " seconds", e);
            sleepSecondsRemoving = DEFAULT_SLEEP_REMOVING;
        }

        return true;
    }

    private Boolean readIsRpi(String isRpiStr, Logger logger) {
        if (null == isRpiStr) {
            return null;
        }

        switch (isRpiStr) {
            case "true":
                return true;
            case "false": isRpi = false;
                return false;
            default:
                logger.error(
                        "can't parse boolean parameter - isRpi. Valid values: \"true\" and \"false\". Actual value: "
                                + isRpiStr);
                return null;
        }
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public String getUrlDb() {
        return urlDb;
    }

    public long getMaxRows() {
        return maxRows;
    }

    public long getSleepSecondsRemoving() {
        return sleepSecondsRemoving;
    }

    public boolean getIsRpi() {
        return isRpi;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public int getPeriod() {
        return period;
    }
}
