package com.company;

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

    boolean load(String arg) {
        Properties prop = new Properties();

        String path = arg.substring(ARG_START.length());

        try(InputStream inputStream = new FileInputStream(path))  {
            prop.load(inputStream);

            ip = prop.getProperty(PROPERTY_IP_SERVER_1);
            if (null == ip) {
                return false;
            }
            System.out.println("IP Digital Ocean = " + ip);

            try {
                port = Integer.parseInt(prop.getProperty(PROPERTY_PORT_DO_SERVER_1));
            } catch (NumberFormatException e) {
                return false;
            }
            System.out.println("Port Digital Ocean " + port);

            urlForSql = prop.getProperty(PROPERTY_URL_FOR_SQL);
            System.out.println("Url for SQL lite" + urlForSql);

        } catch(IOException ex){
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
