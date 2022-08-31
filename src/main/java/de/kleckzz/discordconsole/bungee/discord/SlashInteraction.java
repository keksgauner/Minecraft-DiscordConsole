package de.kleckzz.discordconsole.bungee.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;
import java.util.Objects;

public class SlashInteraction extends ListenerAdapter {

    public static String server;
    public static String command;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Member member = Objects.requireNonNull(event.getMember());
        switch (event.getName()) {
            case "help":
                event.replyEmbeds(
                        getEmbed(member, "You can use the command /select [server] and /send [command]")
                                .build()).queue();
                break;
            case "select":
                server = Objects.requireNonNull(event.getOption("server")).getAsString();
                event.replyEmbeds(
                        getEmbed(member, "You selected server: " + server)
                                .build()).queue();
                break;
            case "send":
                command = Objects.requireNonNull(event.getOption("command")).getAsString();
                event.replyEmbeds(
                        getEmbed(member, "The command " + command + " was sent to the server " + server)
                        .build()).queue();
                break;
            default:
                event.replyEmbeds(
                        getEmbed(member, "WTF. How do you get there?")
                                .build()).queue();
                break;
        }
    }

    private static EmbedBuilder getEmbed(Member member, String message) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode("#5865F2"));
        builder.setTitle("> DiscordServer ");
        builder.addField("Feedback", message, false);
        builder.setFooter("For you " + member.getUser().getName() + " <3");
        builder.setTimestamp(Instant.now());

        return builder;
    }
}
