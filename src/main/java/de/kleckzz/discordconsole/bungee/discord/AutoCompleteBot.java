package de.kleckzz.discordconsole.bungee.discord;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoCompleteBot extends ListenerAdapter {
    private static String[] server = new String[]{"none"};

    private static ArrayList<String> serverList = new ArrayList<>();

    public static void addServer(String serverName) {
        serverList.add(serverName);
        for(int i = 0; i < serverList.size(); i++) {
            server[i] = serverList.get(i);
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (event.getName().equals("select") && event.getFocusedOption().getName().equals("server")) {
            List<Command.Choice> options = Stream.of(server)
                    .filter(server -> server.startsWith(event.getFocusedOption().getValue())) // only display words that start with the user's current input
                    .map(server -> new Command.Choice(server, server)) // map the words to choices
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }
}