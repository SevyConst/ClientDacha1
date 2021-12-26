package com.company;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ForProperties {
    public static final String ARG_START = "-pathConfig=";

    private static final String PROPERTY_IP_DO = "IP_DO";
    private static final String PROPERTY_PORT_DO = "Port_DO";

    private String ip;
    private int port;

    boolean load(String arg) {
        Properties prop = new Properties();

        String path = arg.substring(ARG_START.length());

        try(InputStream inputStream = new FileInputStream(path))  {
            prop.load(inputStream);

            ip = prop.getProperty(PROPERTY_IP_DO);
            if (null == ip) {
                return false;
            }
            System.out.println("IP 1 = " + ip);

            try {
                port = Integer.parseInt(prop.getProperty(PROPERTY_PORT_DO));
            } catch (NumberFormatException e) {
                return false;
            }
            System.out.println("Port 1 = " + port);

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
}
