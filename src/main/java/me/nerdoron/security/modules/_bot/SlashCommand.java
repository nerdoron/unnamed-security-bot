package me.nerdoron.security.modules._bot;

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class SlashCommand {

    public void executeGlobal(SlashCommandInteractionEvent event) {
        final Logger logger = LoggerFactory.getLogger(SlashCommand.class);
        try {
            execute(event);
        } catch (Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    public abstract void execute(SlashCommandInteractionEvent event);

    public abstract SlashCommandData getSlash();

    public String getCategory() {
        String name = this.getClass().getPackage().getName();
        String r = name.split("\\.")[4];
        return r;
    }
}