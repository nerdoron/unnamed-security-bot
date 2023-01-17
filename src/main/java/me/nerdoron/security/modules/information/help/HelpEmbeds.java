package me.nerdoron.security.modules.information.help;

import me.nerdoron.security.Global;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class HelpEmbeds {

        public MessageEmbed mainMenu = new EmbedBuilder()
                        .setTitle("🔮 Help Menu")
                        .setColor(Global.embedColor)
                        .setDescription(
                                        "Click the buttons to view help.")
                        .build();

}
