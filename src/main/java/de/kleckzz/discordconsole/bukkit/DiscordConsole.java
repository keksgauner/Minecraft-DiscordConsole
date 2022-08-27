package de.kleckzz.discordconsole.bukkit;

import de.kleckzz.discordconsole.Data;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public final class DiscordConsole extends JavaPlugin {

    public static Plugin plugin;
    public static JDA jda;

    @Override
    public void onEnable() {
        plugin = this;

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
