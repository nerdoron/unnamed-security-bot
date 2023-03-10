package me.nerdoron.security.modules._bot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import me.nerdoron.security.modules.information.help.HelpButtonHandler;
import net.dv8tion.jda.api.JDA;

public class RegisterEvents {

    public void registerEvents(JDA jda) {
        EventWaiter waiter = new EventWaiter();
        BotCommandsHandler cmdHandler = new BotCommandsHandler(waiter);
        cmdHandler.registerCommandsOnDiscord(jda);
        jda.addEventListener(cmdHandler);
        jda.addEventListener(new HelpButtonHandler());
    }

}
