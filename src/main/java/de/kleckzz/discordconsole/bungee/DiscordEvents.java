package de.kleckzz.discordconsole.bungee;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class DiscordEvents extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message message = event.getMessage();
        if (message.getContentRaw().startsWith("-send"))
        {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            if(message.getContentRaw().split(" ").length != 1) {
                channel.sendMessage("Sending command")
                        .queue(response -> {
                            String command = message.getContentRaw().substring(6);
                            CommandSender sender = DiscordConsole.plugin.getProxy().getConsole();
                            Plugin plugin = DiscordConsole.plugin;
                            ProxyServer.getInstance().getScheduler().runAsync(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    plugin.getLogger().info("Discord send command: " + command);
                                    plugin.getProxy().getPluginManager().dispatchCommand(sender , command);
                                }
                            });

                            response.editMessageFormat("Command %s sent! (%d ms)", command, System.currentTimeMillis() - time).queue();
                        });
            } else {
                channel.sendMessage("Ok Sir...")
                        .queue(response -> {
                            response.editMessageFormat("I cannot do that. Try -send [command] (%d ms)", System.currentTimeMillis() - time).queue();
                        });
            }
        }
    }
}
