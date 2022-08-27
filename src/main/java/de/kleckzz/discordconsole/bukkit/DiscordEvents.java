package de.kleckzz.discordconsole.bukkit;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

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
                            CommandSender sender = DiscordConsole.plugin.getServer().getConsoleSender();
                            Plugin plugin = DiscordConsole.plugin;
                            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    plugin.getLogger().info("Discord send command: " + command);
                                    plugin.getServer().dispatchCommand(sender , command);
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
