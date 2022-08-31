package de.kleckzz.discordconsole.bungee.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;

public class SlashInteraction extends ListenerAdapter {

    public static String server;
    public static String command;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "help":
                event.reply("You can use the command /select [server] and /send [command]");
                break;
            case "select":
                server = event.getOption("server").getAsString();
                event.reply("You selected server: " + server);
                break;
            case "send":
                command = event.getOption("command").getAsString();
                event.reply("The command " + command + " was sent to the server " + server);
                break;
        }
    }

    @Deprecated
    private static EmbedBuilder getEmbed(Member member) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode("#5865F2"));
        builder.setTitle("> Server ");
        builder.setFooter("For you " + member.getUser().getName() + " <3");
        builder.setTimestamp(Instant.now());

        return builder;
    }
}
