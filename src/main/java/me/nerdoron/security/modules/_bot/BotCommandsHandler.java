package me.nerdoron.security.modules._bot;

import java.util.ArrayList;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import me.nerdoron.security.Global;
import me.nerdoron.security.modules.admin.ModifySettingsCommand;
import me.nerdoron.security.modules.admin.SettingsCommand;
import me.nerdoron.security.modules.admin.SetupCommand;
import me.nerdoron.security.modules.information.HelpCommand;
import me.nerdoron.security.modules.information.InfoCommand;
import me.nerdoron.security.modules.mod.RaidModeCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class BotCommandsHandler extends ListenerAdapter {
    public ArrayList<SlashCommand> commands = new ArrayList<>();

    public BotCommandsHandler(EventWaiter waiter) {
        Global.COMMANDS_HANDLER = this;
        commands.add(new InfoCommand());
        commands.add(new HelpCommand(this));
        commands.add(new SetupCommand());
        commands.add(new SettingsCommand());
        commands.add(new ModifySettingsCommand());
        commands.add(new RaidModeCommand());
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
            case "mod":
                return "🛡️ Moderation Commands";
            case "information":
                return "ℹ️ Informative Commands";
            default:
                return null;
        }
    }
}
