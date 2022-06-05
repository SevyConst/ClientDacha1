package com.company;

import com.company.dao.EventsResponse;
import com.company.models.Events;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import org.slf4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class httpClient {

    private static final String END_OF_URL = "/event";
    private final String ip;
    private final int port;
    private final Logger logger;

    public httpClient(String ip, int port, Logger logger) {
        this.ip = ip;
        this.port = port;
        this.logger = logger;
    }


    EventsResponse sendEvents(Events events) {

        HttpURLConnection connection;
        logger.debug("start sending");
        long startTime = System.nanoTime();
        try {
            logger.debug("init connection");
            URL url = new URL(ip + ':' + port + END_OF_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
        } catch (IOException e) {
            logger.error("can't create url or open connection", e);
            return null;
        }

        logger.debug("create outputStream");
        try (OutputStream outputStream = connection.getOutputStream()) {
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(events, Events.class, jsonWriter);
            jsonWriter.flush();
            logger.debug("flushed!");
        } catch (IOException e) {
            logger.error("can't send request", e);
            return null;
        }

        logger.debug("create reader!");
        try (Reader reader = new InputStreamReader(connection.getInputStream())) {
            logger.info("response code: " + connection.getResponseCode());
            EventsResponse eventsResponse = new Gson().fromJson(reader, EventsResponse.class);
            logger.info("response: " + new Gson().toJson(eventsResponse));

            long endTime = System.nanoTime();
            logger.info("Execution time: " + (endTime - startTime)/Math.pow(10, 9) + " milliseconds");
            return eventsResponse;

        } catch (IOException e) {
            logger.error("can't parse response", e);
            return null;
        }
    }
}
