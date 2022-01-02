package com.company;

import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ForProperties {
    public static final String ARG_START = "-pathConfig=";

    private static final String PROPERTY_IP_SERVER_1 = "ip_server_1";
    private static final String PROPERTY_PORT_DO_SERVER_1 = "port_server_1";
    private static final String PROPERTY_URL_FOR_SQL = "url_for_sql";
    private static final String PROPERTY_IS_RASPBERRY_PI = "is_raspberry_pi";

    private String ip;
    private int port;
    private String urlForSql;
    private String isRpi;

    boolean load(String arg, Logger logger) {
        Properties prop = new Properties();

        String path = arg.substring(ARG_START.length());

        try(InputStream inputStream = new FileInputStream(path))  {
            prop.load(inputStream);

            ip = prop.getProperty(PROPERTY_IP_SERVER_1);
            if (null == ip) {
                logger.error("null == ip");
                return false;
            }
            logger.info("IP Digital Ocean = " + ip);

            try {
                port = Integer.parseInt(prop.getProperty(PROPERTY_PORT_DO_SERVER_1));
            } catch (NumberFormatException e) {
                logger.error("can't parse properties: can't read port", e);
                return false;
            }
            logger.info("Port Digital Ocean " + port);

            urlForSql = prop.getProperty(PROPERTY_URL_FOR_SQL);
            logger.info("Url for SQL lite" + urlForSql);

        } catch(IOException e){
            logger.error("can't read properties", e);
            return false;
        }

        return true;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public String getUrlForSql() {
        return urlForSql;
    }

    public String getIsRpi() {
        return isRpi;
    }
}
