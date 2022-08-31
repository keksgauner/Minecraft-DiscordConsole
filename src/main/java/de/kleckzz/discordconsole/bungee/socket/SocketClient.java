package de.kleckzz.discordconsole.bungee.socket;

import de.kleckzz.discordconsole.bungee.DiscordConsole;
import de.kleckzz.discordconsole.bungee.discord.SlashInteraction;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClient {
    public SocketClient(String address, int port, String command)
            throws IOException, InterruptedException {

        InetSocketAddress hostAddress = new InetSocketAddress(address, port);
        SocketChannel client = SocketChannel.open(hostAddress);

        String threadName = Thread.currentThread().getName();

        DiscordConsole.plugin.getLogger().info("[" + threadName + "] Client... started.");

        String messageBuilder = "Command!" + ";" + "TOKEN" + ";" + command;

        byte [] message = new String(messageBuilder).getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(message);
        client.write(buffer);
        DiscordConsole.plugin.getLogger().info("Sending: " + messageBuilder);
        buffer.clear();
        client.close();
    }
}
