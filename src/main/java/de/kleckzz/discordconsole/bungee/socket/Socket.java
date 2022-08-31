package de.kleckzz.discordconsole.bungee.socket;

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
        new Thread(server, "reportingServer").start();
    }
    public static void startClient(String address, int port, String command) {
        Runnable client = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    new SocketClient(address, port, command);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        new Thread(client, "commandTransmitterClient").start();
    }
}
