package io.helidon.example.jms;

import io.helidon.microprofile.server.Server;

public class Main {
    public static void main(String[] args) {
        Server.create().start();
    }
}
