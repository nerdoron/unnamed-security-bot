package me.nerdoron.security.modules.information;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;

import me.nerdoron.security.modules._bot.BotCommandsHandler;
import me.nerdoron.security.modules._bot.SlashCommand;
import me.nerdoron.security.modules.information.help.HelpEmbeds;

public class HelpCommand extends SlashCommand {
    private final BotCommandsHandler handler;
    HelpEmbeds helpEmbeds = new HelpEmbeds();

    public HelpCommand(BotCommandsHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        ArrayList<Button> buttons = new ArrayList<>();
        ArrayList<String> categoriesAdded = new ArrayList<>();
        buttons.add(Button.secondary("HELP:" + "main", "🔮 Main Menu"));

        for (SlashCommand command : handler.commands) {
            String cmdCategory = command.getCategory();
            if (!categoriesAdded.contains(cmdCategory)) {
                String detailedName = handler.getCategoryDetailedName(cmdCategory);
                if (detailedName != null) {
                    categoriesAdded.add(cmdCategory);
                    buttons.add(Button.secondary("HELP:" + cmdCategory, detailedName));
                }
            }
        }

        event.replyEmbeds(helpEmbeds.mainMenu)
                .addActionRow(buttons)
                .setEphemeral(true)
                .queue();
    }

    @Override
    public SlashCommandData getSlash() {
        return Commands.slash("help", "Displays the help menu.");
    }

}
