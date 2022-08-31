package de.kleckzz.discordconsole.bungee;

public class MinecraftServer {
    private final String serverName;
    private final String serverAddress;
    private final int port;

    public MinecraftServer(String serverName, String serverAddress, int port) {
        this.serverName = serverName;
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public String getServerName() {
        return serverName;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getPort() {
        return port;
    }
}
