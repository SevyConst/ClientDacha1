package com.company;

import com.company.dao.EventsResponse;
import com.company.models.ModelEvents;
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


    EventsResponse sendEvents(ModelEvents modelEvents) {

        HttpURLConnection connection;
        try {
            URL url = new URL(ip + ':' + port + END_OF_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
        } catch (IOException e) {
            logger.error("can't create url or open connection", e);
            return null;
        }

        try (OutputStream outputStream = connection.getOutputStream()) {
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(modelEvents, ModelEvents.class, jsonWriter);
            jsonWriter.flush();
        } catch (IOException e) {
            logger.error("can't send request", e);
            return null;
        }

        try (Reader reader = new InputStreamReader(connection.getInputStream())) {
            logger.debug("response code: " + connection.getResponseCode());
            EventsResponse eventsResponse = new Gson().fromJson(reader, EventsResponse.class);
            logger.debug("response: " + new Gson().toJson(eventsResponse));

            return eventsResponse;

        } catch (IOException e) {
            logger.error("can't parse response", e);
            return null;
        }
    }
}
