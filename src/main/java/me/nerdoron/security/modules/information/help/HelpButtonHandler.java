package me.nerdoron.security.modules.information.help;

import me.nerdoron.security.Global;
import me.nerdoron.security.modules._bot.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpButtonHandler extends ListenerAdapter {
    HelpEmbeds helpEmbeds = new HelpEmbeds();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        if (!buttonId.contains("HELP:"))
            return;
        String[] buttonSplit = buttonId.split(":");
        String buttonCategory = buttonSplit[1];

        if (buttonCategory.equals("main")) {
            event.editMessageEmbeds(helpEmbeds.mainMenu).queue();
            return;
        }

        String embedDescription = "A list of all commands under the `" +
                buttonCategory + "` category\n\n";
        for (SlashCommand command : Global.COMMANDS_HANDLER.commands) {
            if (!command.getCategory().equals(buttonCategory))
                continue;

            String commandName = command.getSlash().getName();

            embedDescription += "`/" + commandName + "` - ";
            embedDescription += command.getSlash().getDescription() + "\n";
        }
        EmbedBuilder emb = new EmbedBuilder();
        emb.setTitle(Global.COMMANDS_HANDLER.getCategoryDetailedName(buttonCategory));
        emb.setDescription(embedDescription);
        emb.setColor(Global.embedColor);
        event.editMessageEmbeds(emb.build()).queue();
    }
}
