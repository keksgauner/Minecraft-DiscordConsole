package de.kleckzz.discordconsole.bukkit.socket;

import java.io.IOException;

public class Socket {
    public static void startServer(String address, int port) {
        Runnable server = new Runnable() {
            @Override
            public void run() {
                try {
                    new SocketServer(address, port);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        new Thread(server, "commandReceiveServer").start();
    }
    public static void startClient(String address, int port) {
        Runnable client = new Runnable() {
            @Override
            public void run() {
                try {
                    new SocketClient(address, port);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        new Thread(client, "feedbackClient").start();
    }
}