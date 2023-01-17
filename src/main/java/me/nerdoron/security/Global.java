package me.nerdoron.security;

import io.github.cdimascio.dotenv.Dotenv;
import me.nerdoron.security.modules._bot.BotCommandsHandler;

import java.awt.Color;

public class Global {
    public static Dotenv dotenv = Dotenv.load();
    public static Color embedColor = Color.decode("#2f3136");
    public static BotCommandsHandler COMMANDS_HANDLER;

}
