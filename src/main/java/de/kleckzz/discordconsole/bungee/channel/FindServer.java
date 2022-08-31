package de.kleckzz.discordconsole.bungee.channel;

import de.kleckzz.coresystem.proxy.libraries.plugin.channel.PluginChannel;
import de.kleckzz.discordconsole.bungee.DiscordConsole;
import de.kleckzz.discordconsole.bungee.discord.AutoCompleteBot;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.Map;

public class FindServer implements Listener {
    public static void callServer() throws IllegalClassFormatException, IOException {
        Map<String,ServerInfo> servers = DiscordConsole.plugin.getProxy().getConfig().getServersCopy();

        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(DiscordConsole.config.getConfig().getString("trusted-token"));
        arrayList.add("Hello!");

        for(ServerInfo serverInfo : servers.values()) {
            DiscordConsole.pluginChannelProxy.sendMessage(serverInfo, arrayList);
        }
    }

    @EventHandler
    public void getHello(PluginChannel event) {
        try {
            ArrayList<Object> arrayList = event.getStringArray();
            String trustedToken =  (String) arrayList.get(0);
            String name =  (String) arrayList.get(1);

            if(trustedToken == DiscordConsole.config.getConfig().getString("trusted-token")) {
                AutoCompleteBot.addServer(name);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
