package com.company;

public class Controller {

    private final Sqlite sqlite;
    private final Client client;

    public Controller(Sqlite sqlite, Client client) {
        this.sqlite = sqlite;
        this.client = client;
    }

    void work() {
        sqlite.insertStart();
    }
}
