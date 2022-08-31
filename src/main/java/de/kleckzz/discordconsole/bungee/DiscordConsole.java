package de.kleckzz.discordconsole.bungee;

import de.kleckzz.coresystem.bukkit.libraries.plugin.ConfigAccessor;
import de.kleckzz.coresystem.proxy.libraries.plugin.ConfigAccessorBungee;
import de.kleckzz.coresystem.proxy.libraries.plugin.JsonAccessorBungee;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.md_5.bungee.api.plugin.Plugin;

import javax.security.auth.login.LoginException;

public final class DiscordConsole extends Plugin {

    public static Plugin plugin;
    public static JDA jda;
    public static ConfigAccessorBungee config;

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

        Boolean setup = config.getConfig().getBoolean("setup");
        if(setup == true) {
            plugin.getLogger().warning("You have to setup this plugin");
            plugin.getLogger().warning("You have to insert the discord token from your bot in the DiscordConsole/config.json from https://discord.com/developers/applications. Don't forget to set setup to false!");
        } else {
            String token = config.getConfig().getString("token");
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

    private void startDiscordBot(String token) throws LoginException, InterruptedException {
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
