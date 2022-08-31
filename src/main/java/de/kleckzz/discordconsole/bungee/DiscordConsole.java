package de.kleckzz.discordconsole.bungee;

import de.kleckzz.coresystem.proxy.libraries.plugin.ConfigAccessorBungee;
import de.kleckzz.coresystem.proxy.libraries.plugin.InitializeManager;
import de.kleckzz.coresystem.proxy.libraries.plugin.channel.PluginChannelProxy;
import de.kleckzz.discordconsole.bungee.channel.FindServer;
import de.kleckzz.discordconsole.bungee.discord.AutoCompleteBot;
import de.kleckzz.discordconsole.bungee.discord.SlashInteraction;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.md_5.bungee.api.plugin.Plugin;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.security.SecureRandom;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public final class DiscordConsole extends Plugin {

    public static Plugin plugin;
    public static ConfigAccessorBungee config;
    public static PluginChannelProxy pluginChannelProxy;

    // characters to use to build a token
    private static final String TOKEN_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Override
    public void onEnable() {
        plugin = this;

        plugin.getLogger().info("\u00A7aWelcome to the DiscordConsole plugin");
        if(plugin.getDescription().getVersion().contains("SNAPSHOT")) {
            plugin.getLogger().warning("You are running a development version of the plugin, please report any bugs to GitHub.");
        }

        loadConfig();
        loadToken();

        if(checkDiscordEnabled()) {
            try {
                plugin.getLogger().info("Starting Discord Bot...");
                String token = config.getConfig().getString("discord.token");
                startDiscordBot(token);
                setupPluginChannel();
            } catch (LoginException | InterruptedException e) {
                if(e.getMessage().contains("The provided token is invalid!")) {
                    config.getConfig().set("discord.enabled", false);
                    config.saveConfig();
                    plugin.getLogger().warning("The provided token is invalid! I'll set discord.enabled to false");
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

    private void loadConfig() {
        plugin.getLogger().info("I am loading the config. Please wait...");
        config = new ConfigAccessorBungee(plugin, "config.yml");
        config.saveDefaultConfig();
        plugin.getLogger().info("The config is loaded");
    }

    private void loadToken() {
        if(config.getConfig().getString("trusted-token").equals("generate")) {
            plugin.getLogger().info("I'll create a trusted token just for you!");
            config.getConfig().set("trusted-token", generateToken());
            config.saveConfig();
        }
    }

    private void setupPluginChannel() {
        pluginChannelProxy = new PluginChannelProxy(plugin);
        pluginChannelProxy.registerOutgoingPluginChannel("dc:cmd");
        pluginChannelProxy.registerIncomingPluginChannel("dc:feedback");

        InitializeManager.registerEvent(plugin, new FindServer());

        searchServer();
    }

    private void searchServer() {
        AutoCompleteBot.addServer(config.getConfig().getString("serverName"));
        try {
            FindServer.callServer();
        } catch (IllegalClassFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if discord enabled
     * @return if the discord enabled
     */
    private boolean checkDiscordEnabled() {
        boolean discordEnabled = config.getConfig().getBoolean("discord.enabled");
        if(!discordEnabled) {
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
                Commands.slash("select", "Select the server")
                        .addOption(STRING, "server", "The name of the Server", true, true)
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

        builder.addEventListeners(new DiscordEvents());
        builder.addEventListeners(new AutoCompleteBot());
        builder.addEventListeners(new SlashInteraction());

        return builder.build();
    }
}
