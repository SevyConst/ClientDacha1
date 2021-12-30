package com.company;

import com.company.models.Event;
import com.company.models.EventsResponse;
import com.company.models.Events;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class Client {

    private static final String END_OF_URL = "/events";
    private final String ip;
    private final int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }


    private void sendEvents(Events events) {
        try {
            URL url = new URL(ip + END_OF_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            Event event = new Event();


            try (OutputStream outputStream = connection.getOutputStream()) {
                JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(event, Event.class, jsonWriter);
                jsonWriter.flush();
            } catch (IOException e) {

            }

            System.out.println(connection.getResponseCode());

            try (Reader reader = new InputStreamReader(connection.getInputStream())) {
                EventsResponse eventsResponse = new Gson().fromJson(reader, EventsResponse.class);


                System.out.println("response: " + new Gson().toJson(eventsResponse));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException | NullPointerException e) {
            System.out.println();
            e.printStackTrace();
        }
    }
}
