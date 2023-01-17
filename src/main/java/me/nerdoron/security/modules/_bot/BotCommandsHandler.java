package me.nerdoron.security.modules._bot;

import java.util.ArrayList;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import me.nerdoron.security.Global;
import me.nerdoron.security.modules.info.InfoCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class BotCommandsHandler extends ListenerAdapter {
    public ArrayList<SlashCommand> commands = new ArrayList<>();

    public BotCommandsHandler(EventWaiter waiter) {
        Global.COMMANDS_HANDLER = this;
        commands.add(new InfoCommand());
        // commands.add(new Class());

    }

    public void registerCommandsOnDiscord(JDA jda) {
        ArrayList<CommandData> slashCommands = new ArrayList<>();
        for (SlashCommand command : commands) {
            slashCommands.add(command.getSlash());
        }
        jda.updateCommands().addCommands(slashCommands).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        for (SlashCommand command : commands) {
            if (command.getSlash().getName().equals(commandName)) {
                command.execute(event);
                break;
            }
        }
    }

    public String getCategoryDetailedName(String category) {
        switch (category) {
            case "admin":
                return "⚙️ Administration Commands";
            case "moderation":
                return "🛡️ Moderation Commands";
            case "info":
                return "ℹ️ Informative Commands";
            default:
                return null;
        }
    }
}
