package de.kleckzz.discordconsole.bungee.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;
import java.util.Objects;

public class SlashInteraction extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "select":
                event.deferReply()
                        .queue();
                event.getChannel().sendMessageEmbeds(
                        getEmbed(Objects.requireNonNull(event.getMember()))
                        .build())
                        .queue();
                break;
            case "send":
                break;
        }
    }

    private static EmbedBuilder getEmbed(Member member) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode("#5865F2"));
        builder.setTitle("> Server ");
        builder.setFooter("For you " + member.getUser().getName() + " <3");
        builder.setTimestamp(Instant.now());

        return builder;
    }
}
