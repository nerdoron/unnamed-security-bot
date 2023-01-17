package me.nerdoron.security.modules.admin;

import me.nerdoron.security.Global;
import me.nerdoron.security.modules._bot.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class SettingsCommand extends SlashCommand {
    SettingsSQL settingsSQL = new SettingsSQL();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!(event.getMember().hasPermission(Permission.ADMINISTRATOR)))
            return;
        String guildId = event.getGuild().getId();

        // check if guild is setup
        if (!(settingsSQL.isGuildSetup(guildId))) {
            event.reply("This guild is not setup! To set up your guild please run `/setup`.")
                    .setEphemeral(true).queue();
            return;
        }

        event.replyEmbeds(getSettingsEmbed(guildId, event)).setEphemeral(true).queue();

    }

    public MessageEmbed getSettingsEmbed(String guildId, SlashCommandInteractionEvent event) {
        TextChannel alertsChannel = event.getGuild().getTextChannelById(settingsSQL.getflagsChannelId(guildId));
        Role modRole = event.getGuild().getRoleById(settingsSQL.getRoleId(guildId));
        String pingMods = settingsSQL.getPingMods(guildId);
        String autoScan = settingsSQL.getAutoScan(guildId);

        MessageEmbed settingsEmbed = new EmbedBuilder()
                .setTitle("Guild Settings")
                .setThumbnail(event.getGuild().getIconUrl())
                .setDescription("Settings for guild `" + event.getGuild().getName() + "`:")
                .addField("Alerts Channel", alertsChannel.getAsMention(), false)
                .addField("Mod Role", modRole.getAsMention(), false)
                .addField("Ping Mods on Alert", pingMods, false)
                .addField("Auto-Scan", autoScan, false)
                .setColor(Global.embedColor)
                .build();
        return settingsEmbed;
    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData settings = Commands.slash("settings", "Show this server's settings.");
        settings.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        return settings;

    }

}
