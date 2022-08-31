package de.kleckzz.discordconsole.bukkit.socket;

import de.kleckzz.discordconsole.bukkit.DiscordConsole;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClient {
    public SocketClient(String address, int port)
            throws IOException, InterruptedException {

        InetSocketAddress hostAddress = new InetSocketAddress(address, port);
        SocketChannel client = SocketChannel.open(hostAddress);

        String threadName = Thread.currentThread().getName();

        DiscordConsole.plugin.getLogger().info("[" + threadName + "] Client... started.");

        String serverName = DiscordConsole.config.getConfig().getString("serverName");

        String serverAddress = DiscordConsole.config.getConfig().getString("socket.server.address");
        int serverPort = DiscordConsole.config.getConfig().getInt("socket.server.port");
        String token = DiscordConsole.config.getConfig().getString("socket.trusted-token");

        String messageBuilder = "Hello!" + ";" + token + ";" + serverName + ";" + serverAddress + ";" + serverPort;

        byte [] message = new String(messageBuilder).getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(message);
        client.write(buffer);
        DiscordConsole.plugin.getLogger().info("Sending: " + messageBuilder);
        buffer.clear();
        client.close();
        DiscordConsole.plugin.getLogger().info("Informed the server about me!");
    }
}
