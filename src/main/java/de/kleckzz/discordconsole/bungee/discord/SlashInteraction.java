package de.kleckzz.discordconsole.bungee.discord;

import de.kleckzz.discordconsole.bungee.DiscordConsole;
import de.kleckzz.discordconsole.bungee.MinecraftServer;
import de.kleckzz.discordconsole.bungee.socket.Socket;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.awt.*;
import java.time.Instant;
import java.util.Objects;

public class SlashInteraction extends ListenerAdapter {

    public static String server = null;
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
                if(server == null) {
                    event.replyEmbeds(
                            getEmbed(member, "Select first your server!")
                                    .build()).queue();
                    return;
                }
                command = Objects.requireNonNull(event.getOption("command")).getAsString();

                if(server.equals(DiscordConsole.config.getConfig().getString("serverName"))) {
                    CommandSender sender = DiscordConsole.plugin.getProxy().getConsole();
                    Plugin plugin = DiscordConsole.plugin;
                    ProxyServer.getInstance().getScheduler().runAsync(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.getLogger().info("Discord got to send command: " + command);
                            plugin.getProxy().getPluginManager().dispatchCommand(sender , command);
                        }
                    });
                } else {
                    for(MinecraftServer minecraftServer : AutoCompleteBot.serverList) {
                        if(minecraftServer.getServerName().equals(server)) {
                            Socket.startClient(minecraftServer.getServerAddress(), minecraftServer.getPort(), command);
                        }
                    }
                }

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
