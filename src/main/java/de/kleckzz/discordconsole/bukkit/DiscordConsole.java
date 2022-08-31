package de.kleckzz.discordconsole.bukkit;

import de.kleckzz.coresystem.bukkit.libraries.plugin.ConfigAccessor;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiscordConsole extends JavaPlugin {

    public static JavaPlugin plugin;
    public static ConfigAccessor config;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.getLogger().info("\u00A7aWelcome to the DiscordConsole plugin");
        if(plugin.getDescription().getVersion().contains("SNAPSHOT")) {
            plugin.getLogger().warning("You are running a development version of the plugin, please report any bugs to GitHub.");
        }

        plugin.getLogger().info("I am loading the config. Please wait...");
        config = new ConfigAccessor(plugin, "server.yml");
        config.saveDefaultConfig();
        plugin.getLogger().info("The config is loaded");

        if(config.getConfig().getBoolean("setup") == true) {
            plugin.getLogger().warning("You have to setup this plugin!");
        }

        plugin.getLogger().info("\u00A7aThe plugin DiscordConsole finished loading :)");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void setConfig() {

        /*
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
        plugin.saveConfig();*/
    }
}
