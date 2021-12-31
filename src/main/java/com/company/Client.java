package com.company;

import com.company.models.Events;
import com.company.models.EventsResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class Client {

    private static final String END_OF_URL = ":8080/events";
    private final String ip;
    private final int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }


    EventsResponse sendEvents(Events events) {
        try {
            URL url = new URL(ip + END_OF_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream()) {
                JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(events, Events.class, jsonWriter);
                jsonWriter.flush();
            } catch (IOException e) {
                return null;
            }

            System.out.println(connection.getResponseCode());

            try (Reader reader = new InputStreamReader(connection.getInputStream())) {
                EventsResponse eventsResponse = new Gson().fromJson(reader, EventsResponse.class);
                System.out.println("response: " + new Gson().toJson(eventsResponse));

                return eventsResponse;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
