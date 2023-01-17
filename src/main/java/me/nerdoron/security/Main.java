package me.nerdoron.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nerdoron.security.modules._bot.RegisterEvents;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main {
    static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static JDA jda;

    public static void main(String[] args) {
        logger.info("Starting and logging in.");
        String token = Global.dotenv.get("TOKEN");
        try {
            jda = JDABuilder.create(
                    token,
                    GatewayIntent.DIRECT_MESSAGES,
                    GatewayIntent.GUILD_BANS,
                    GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_VOICE_STATES,
                    GatewayIntent.GUILD_PRESENCES)
                    .enableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
                    .disableCache(CacheFlag.SCHEDULED_EVENTS)
                    .build();
            logger.info("Logged in as " + jda.getSelfUser().getAsTag());
            jda.getPresence().setActivity(Activity.watching("Your server. | /info"));

        } catch (IllegalArgumentException ex) {
            logger.error("Error while logging in\n" + ex.getMessage());
            return;
        }

        RegisterEvents registerEvents = new RegisterEvents();
        registerEvents.registerEvents(jda);
    }

}

/*
 * TODO:
 * - Main database
 * - Yarin's Algorithm
 * - Anti Raid Mode
 * - Scanning
 * - Scam Detection (Scam messages, discord links etc)
 */
