package de.kleckzz.discordconsole.bukkit.channel;

import de.kleckzz.coresystem.bukkit.libraries.plugin.chanel.PluginChannel;
import de.kleckzz.discordconsole.bungee.DiscordConsole;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.util.ArrayList;

public class SayHello implements Listener {
    @EventHandler
    public void gotMessage(PluginChannel event) {
        try {
            ArrayList<Object> arrayList = event.getStringArray();
            String response = String.valueOf(arrayList.get(0));
            String message = String.valueOf(arrayList.get(0));

            String trustedToken = DiscordConsole.config.getConfig().getString("trusted-token");

            if(response.equals(trustedToken) && message.equals("Hello!")) {
                event.getPlayer().sendMessage(DiscordConsole.config.getConfig().getString("serverName"));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
