package me.nerdoron.security.modules.information;

import java.lang.management.ManagementFactory;

import me.nerdoron.security.Global;
import me.nerdoron.security.modules._bot.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class InfoCommand extends SlashCommand {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        MessageEmbed infoEmbed = new EmbedBuilder()
                .setColor(Global.embedColor)
                .setTitle("unnamed security bot")
                .setDescription(
                        "Unnamed security bot is a bot aimed to protect your servers from scammers, raiders and trolls. Our number one priority is protecting your server.")
                .addField("Ping", event.getJDA().getGatewayPing() + "ms.", true)
                .addField("Uptime", getUptime(), true)
                .addField("Library", "[JDA](https://github.com/DV8FromTheWorld/JDA)", true)
                .build();
        event.replyEmbeds(infoEmbed).setEphemeral(true).queue();
    }

    public static String getUptime() {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        long uptimeSec = uptime / 1000;
        long hours = uptimeSec / (60 * 60);
        long minutes = (uptimeSec / 60) - (hours * 60);
        long seconds = uptimeSec % 60;

        String uptimeString = (Long.toString(hours) + " hours, " + Long.toString(minutes) + " minutes, "
                + Long.toString(seconds) + " seconds.");

        return uptimeString;
    }

    @Override
    public SlashCommandData getSlash() {
        return Commands.slash("info", "Information about the bot.");
    }

}
