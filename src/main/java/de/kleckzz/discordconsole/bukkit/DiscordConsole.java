package de.kleckzz.discordconsole.bukkit;

import de.kleckzz.coresystem.bukkit.libraries.plugin.ConfigAccessor;
import de.kleckzz.discordconsole.bukkit.socket.Socket;
import de.kleckzz.discordconsole.bukkit.discord.SlashInteraction;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

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
        if(checkDiscordEnabled()) {
            try {
                plugin.getLogger().info("Starting Discord Bot...");
                String token = config.getConfig().getString("discord.token");
                startDiscordBot(token);
            } catch (LoginException | InterruptedException e) {
                if(e.getMessage().contains("The provided token is invalid!")) {
                    config.getConfig().set("discord.enabled", false);
                    config.saveConfig();
                    plugin.getLogger().warning("The provided token is invalid! I'll set discord.enabled to false");
                }
                throw new RuntimeException(e);
            }
        } else
        if(checkTrustedToken()) {
            Socket.startClient(config.getConfig().getString("socket.client.address"), config.getConfig().getInt("socket.client.port"));
            Socket.startServer(config.getConfig().getString("socket.server.address"), config.getConfig().getInt("socket.server.port"));
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
    /**
     * Check if discord enabled
     * @return if the discord enabled
     */
    private boolean checkDiscordEnabled() {
        boolean discordEnabled = config.getConfig().getBoolean("discord.enabled");
        if(!discordEnabled) {
            plugin.getLogger().warning("If you use the Proxy you can ignore that");
            plugin.getLogger().warning("You have to setup this plugin!");
            plugin.getLogger().warning("You have to insert the discord token from your bot in the DiscordConsole/config.json.");
            plugin.getLogger().warning("Look to https://discord.com/developers/applications to get the token. Don't forget to set the discord.enabled to true!");
            return false;
        }
        return true;
    }

    private void startDiscordBot(String token) throws LoginException, InterruptedException {
        JDA jda = buildDiscordBot(token);
        jda.awaitReady();
        slashCommandDiscordBot(jda.updateCommands());
    }

    private void slashCommandDiscordBot(CommandListUpdateAction commands) {

        commands.addCommands(
                Commands.slash("help", "Shows information about the bot")
        ).queue();

        commands.addCommands(
                Commands.slash("send", "Send a command to the server")
                        .addOption(STRING, "command", "The command what should be to sent", true, false)
        ).queue();
    }

    private JDA buildDiscordBot(String token) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT);

        builder.disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setCompression(Compression.NONE);

        builder.setActivity(Activity.watching(config.getConfig().getString("discord.watching")));

        builder.addEventListeners(new SlashInteraction());

        return builder.build();
    }
}
