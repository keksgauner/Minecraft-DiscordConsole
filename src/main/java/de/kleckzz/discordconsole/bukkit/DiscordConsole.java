package de.kleckzz.discordconsole.bukkit;

import de.kleckzz.discordconsole.Data;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public final class DiscordConsole extends JavaPlugin {

    public static Plugin plugin;
    public static FileConfiguration fileConfiguration;
    public static JDA jda;

    @Override
    public void onEnable() {
        plugin = this;

        setConfig();

        try {
            build(Data.token);
        } catch (LoginException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void setConfig() {
        fileConfiguration = plugin.getConfig();

        // Global info
        fileConfiguration.set("serverName", "server1");

        // Discord path
        fileConfiguration.set("discord.enabled", "false");
        fileConfiguration.set("discord.token", "no-token");


        // Socket path | Server
        fileConfiguration.set("socket.server.enabled", "false");
        fileConfiguration.set("socket.server.address", "localhost");
        fileConfiguration.set("socket.server.port", "8080");

        // Socket path | CLIENT
        fileConfiguration.set("socket.client.enabled", "false");
        fileConfiguration.set("socket.client.address", "localhost");
        fileConfiguration.set("socket.client.port", "8081");

        plugin.getConfig().setDefaults(fileConfiguration);
        plugin.saveConfig();
    }

    private void build(String token) throws LoginException, InterruptedException {
        JDABuilder builder = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT);

        builder.disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setCompression(Compression.NONE);

        builder.setActivity(Activity.watching("Chillt"));

        builder.addEventListeners(new DiscordEvents());

        jda = builder.build();
        jda.awaitReady();
    }
}
