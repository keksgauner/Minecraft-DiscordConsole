package de.kleckzz.discordconsole.bungee;

import de.kleckzz.coresystem.proxy.libraries.plugin.ConfigAccessorBungee;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.md_5.bungee.api.plugin.Plugin;

import javax.security.auth.login.LoginException;
import java.security.SecureRandom;

public final class DiscordConsole extends Plugin {

    public static Plugin plugin;
    public static ConfigAccessorBungee config;

    // characters to use to build a token
    private static final String TOKEN_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Override
    public void onEnable() {
        plugin = this;

        plugin.getLogger().info("\u00A7aWelcome to the DiscordConsole plugin");
        if(plugin.getDescription().getVersion().contains("SNAPSHOT")) {
            plugin.getLogger().warning("You are running a development version of the plugin, please report any bugs to GitHub.");
        }

        plugin.getLogger().info("I am loading the config. Please wait...");
        config = new ConfigAccessorBungee(plugin, "config.yml");
        config.saveDefaultConfig();
        plugin.getLogger().info("The config is loaded");

        if(config.getConfig().getString("trusted-token", null) == null) {
            plugin.getLogger().info("I'll create a trusted token for you!");
            config.getConfig().set("trusted-token", generateToken());
            config.saveConfig();
        }

        if(config.getConfig().getBoolean("setup")) {
            plugin.getLogger().warning("You have to setup this plugin!");
            plugin.getLogger().warning("You have to insert the discord token from your bot in the DiscordConsole/config.json.");
            plugin.getLogger().warning("Look to https://discord.com/developers/applications to get the token. Don't forget to set the setup to false!");
        } else if(config.getConfig().getBoolean("discord.enabled")) {
            String token = config.getConfig().getString("discord.token");
            try {
                startDiscordBot(token);
            } catch (LoginException | InterruptedException e) {
                if(e.getMessage().contains("The provided token is invalid!")) {
                    config.getConfig().set("setup", true);
                    config.saveConfig();
                    plugin.getLogger().warning("The provided token is invalid! I'll set setup to true");
                }
                throw new RuntimeException(e);
            }
        }
        plugin.getLogger().info("\u00A7aThe plugin DiscordConsole finished loading :)");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Randomly generates a new token
     *
     * @return a new token
     */
    private static String generateToken() {
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 64; i++) {
            sb.append(TOKEN_CHARS.charAt(random.nextInt(TOKEN_CHARS.length())));
        }
        return sb.toString();
    }

    private void startDiscordBot(String token) throws LoginException, InterruptedException {
        JDABuilder builder = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT);

        builder.disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setCompression(Compression.NONE);

        builder.setActivity(Activity.watching(config.getConfig().getString("discord.watching")));

        builder.addEventListeners(new DiscordEvents());

        JDA jda = builder.build();
        jda.awaitReady();
    }
}
