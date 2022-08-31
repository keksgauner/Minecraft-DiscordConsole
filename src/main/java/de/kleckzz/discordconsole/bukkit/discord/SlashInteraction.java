package de.kleckzz.discordconsole.bukkit.discord;

import de.kleckzz.discordconsole.bukkit.DiscordConsole;
import de.kleckzz.discordconsole.bungee.MinecraftServer;
import de.kleckzz.discordconsole.bungee.socket.Socket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.time.Instant;
import java.util.Objects;

public class SlashInteraction extends ListenerAdapter {
    public static String command;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Member member = Objects.requireNonNull(event.getMember());
        switch (event.getName()) {
            case "help":
                event.replyEmbeds(
                        getEmbed(member, "You can use the command /send [command]")
                                .build()).queue();
                break;
            case "send":
                command = Objects.requireNonNull(event.getOption("command")).getAsString();

                CommandSender sender = de.kleckzz.discordconsole.bukkit.DiscordConsole.plugin.getServer().getConsoleSender();
                Plugin plugin = DiscordConsole.plugin;
                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        plugin.getLogger().info("Discord got to send command: " + command);
                        plugin.getServer().dispatchCommand(sender , command);
                    }
                });

                event.replyEmbeds(
                        getEmbed(member, "The command " + command + " was sent to the server")
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
