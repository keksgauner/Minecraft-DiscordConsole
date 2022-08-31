package de.kleckzz.discordconsole.bungee.discord;

import de.kleckzz.discordconsole.bungee.DiscordConsole;
import de.kleckzz.discordconsole.bungee.MinecraftServer;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoCompleteBot extends ListenerAdapter {
    private static String[] server = new String[]{"none"};

    public static ArrayList<MinecraftServer> serverList = new ArrayList<>();

    public static void addServer(MinecraftServer minecraftServer) {
        if(!serverList.contains(minecraftServer)) {
            serverList.add(minecraftServer);
            // Change the String[] size
            server = new String[serverList.size()];
            for (int i = 0; i < serverList.size(); i++) {
                server[i] = serverList.get(i).getServerName();
            }
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if(!event.getChannel().getId().equals(DiscordConsole.config.getConfig().getString("discord.channelID")))
            return;
        if (event.getName().equals("select") && event.getFocusedOption().getName().equals("server")) {
            List<Command.Choice> options = Stream.of(server)
                    .filter(server -> server.startsWith(event.getFocusedOption().getValue())) // only display words that start with the user's current input
                    .map(server -> new Command.Choice(server, server)) // map the words to choices
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }
}