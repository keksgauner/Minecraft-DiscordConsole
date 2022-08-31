package de.kleckzz.discordconsole.bukkit;

import de.kleckzz.coresystem.bukkit.libraries.plugin.ConfigAccessor;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
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

        loadConfig();
        if(checkTrustedToken()) {
            plugin.getLogger().info("Say to the proxy hello!");
            // TODO: setup client say hello
        }

        plugin.getLogger().info("\u00A7aThe plugin DiscordConsole finished loading :)");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadConfig() {
        plugin.getLogger().info("I am loading the config. Please wait...");
        config = new ConfigAccessor(plugin, "server.yml");
        config.saveDefaultConfig();
        plugin.getLogger().info("The config is loaded");
    }

    /**
     * Check if the trusted-token on change
     * @return if this invalid
     */
    private boolean checkTrustedToken() {
        if(config.getConfig().getString("socket.trusted-token").equals("change")) {
            plugin.getLogger().warning("You have to setup this plugin!");
            plugin.getLogger().warning("Your trusted token is not setup. You have to copy the token from the proxy!");
            return false;
        }
        return true;
    }
}
