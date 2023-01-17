package me.nerdoron.security.modules.mod;

import me.nerdoron.security.Global;
import me.nerdoron.security.modules._bot.SlashCommand;
import me.nerdoron.security.modules.admin.SettingsSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class RaidModeCommand extends SlashCommand {
    SettingsSQL settingsSQL = new SettingsSQL();
    RaidSQL raidSQL = new RaidSQL();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        String guildId = event.getGuild().getId();
        if (!(settingsSQL.isGuildSetup(guildId))) {
            event.reply("This guild is not setup! To set up your guild please run `/setup`.")
                    .setEphemeral(true).queue();
            return;
        }

        TextChannel alertChannel = guild.getTextChannelById(settingsSQL.getflagsChannelId(guildId));
        Role mods = guild.getRoleById(settingsSQL.getRoleId(guildId));
        String pingModsValue = settingsSQL.getPingMods(guildId);
        Boolean pingMods;
        if (pingModsValue.equalsIgnoreCase("true"))
            pingMods = true;
        else {
            pingMods = false;
        }

        if (raidSQL.checkIfGuildIsInTable(guildId)) {
            turnOff(guildId, pingMods, alertChannel, mods, event);
            event.reply("Disabled raid mode.").setEphemeral(true).queue();
        } else {
            turnOn(guildId, pingMods, alertChannel, mods, event);
            event.reply("Enabled raid mode.").setEphemeral(true).queue();

        }
    }

    public void turnOn(String guildId, Boolean pingMods, TextChannel alertsChannel, Role mods,
            SlashCommandInteractionEvent event) {
        raidSQL.addGuildToTable(guildId);
        MessageEmbed turnOnEmbed = new EmbedBuilder()
                .setTitle("🚨 RAID MODE ENABLED")
                .setDescription(
                        "Raid mode enabled by " + event.getMember().getAsMention()
                                + ". \nAll new members will be kicked until this is turned off.")
                .setColor(Global.embedColor)
                .build();

        if (pingMods) {
            alertsChannel.sendMessage(mods.getAsMention()).addEmbeds(turnOnEmbed).queue();
        } else {
            alertsChannel.sendMessageEmbeds(turnOnEmbed).queue();
        }
    }

    public void turnOff(String guildId, Boolean pingMods, TextChannel alertsChannel, Role mods,
            SlashCommandInteractionEvent event) {
        raidSQL.removeGuildFromTable(guildId);
        MessageEmbed turnOffEmbed = new EmbedBuilder()
                .setTitle("Raid mode disabled.")
                .setDescription(
                        "Raid mode disabled by " + event.getMember().getAsMention())
                .setColor(Global.embedColor)
                .build();

        if (pingMods) {
            alertsChannel.sendMessage(mods.getAsMention()).addEmbeds(turnOffEmbed).queue();
        } else {
            alertsChannel.sendMessageEmbeds(turnOffEmbed).queue();
        }
    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData setup = Commands.slash("raidmode", "Toggle Raid Mode.");
        setup.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS));
        return setup;
    }

}
