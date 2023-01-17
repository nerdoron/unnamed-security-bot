package me.nerdoron.security.modules.admin;

import java.awt.Color;

import me.nerdoron.security.modules._bot.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class ModifySettingsCommand extends SlashCommand {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!(event.getMember().hasPermission(Permission.ADMINISTRATOR)))
            return;
        String subcmd = event.getSubcommandName();
        String guildId = event.getGuild().getId();
        SettingsSQL settingsSQL = new SettingsSQL();
        SettingsCommand settingsCommand = new SettingsCommand();

        // check if guild is setup
        if (!(settingsSQL.isGuildSetup(guildId))) {
            event.reply("This guild is not setup! To set up your guild please run `/setup`.")
                    .setEphemeral(true).queue();
            return;
        }

        // modify alerts channel
        if (subcmd.equals("alertschannel")) {
            Channel channel = event.getInteraction().getOption("channel").getAsChannel();
            if (!(channel.getType().equals(ChannelType.TEXT))) {
                MessageEmbed channelErrorEmbed = new EmbedBuilder()
                        .setTitle("Error!")
                        .setDescription(
                                "The channel you selected isn't a Text Channel. \n\n Please re-run the command and select a text channel.")
                        .setColor(Color.red)
                        .build();
                event.replyEmbeds(channelErrorEmbed).setEphemeral(true).queue();
                return;
            }
            settingsSQL.updateAlertsChannel(channel.getId(), guildId);
            event.reply("Modified your alerts channel.").addEmbeds(settingsCommand.getSettingsEmbed(guildId, event))
                    .setEphemeral(true).queue();
            return;
        }

        // modify role
        if (subcmd.equals("modrole")) {
            Role role = event.getInteraction().getOption("role").getAsRole();
            if ((role.isPublicRole())) {
                MessageEmbed roleErrorEmbed = new EmbedBuilder()
                        .setTitle("Error!")
                        .setDescription(
                                "You selected `@everyone` instead of a role. \n\n Please re-run the command and select a role.")
                        .setColor(Color.red)
                        .build();
                event.replyEmbeds(roleErrorEmbed).setEphemeral(true).queue();
                return;
            }
            settingsSQL.updateModRole(role.getId(), guildId);
            event.reply("Modified your role channel.").addEmbeds(settingsCommand.getSettingsEmbed(guildId, event))
                    .setEphemeral(true).queue();
            return;

        }
        if (subcmd.equals("ping")) {
            Boolean ping = event.getInteraction().getOption("pingmods").getAsBoolean();

            settingsSQL.updatePing(ping, guildId);
            event.reply("Modified your ping setting.").addEmbeds(settingsCommand.getSettingsEmbed(guildId, event))
                    .setEphemeral(true).queue();
            return;
        }

        if (subcmd.equals("autoscan")) {
            Boolean autoScan = event.getInteraction().getOption("scan").getAsBoolean();

            settingsSQL.updateAutoScan(autoScan, guildId);
            event.reply("Modified your Auto-Scan setting.").addEmbeds(settingsCommand.getSettingsEmbed(guildId, event))
                    .setEphemeral(true).queue();
            return;
        }
    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData modifySettings = Commands.slash("modifysetting", "Modify a guild setting.");
        SubcommandData ms_channel = new SubcommandData("alertschannel", "Modify the alerts channel.");
        ms_channel.addOption(OptionType.CHANNEL, "channel", "Select the channel you would like to change to.", true);
        SubcommandData ms_modrole = new SubcommandData("modrole", "Modify the moderation role.");
        ms_modrole.addOption(OptionType.ROLE, "role", "Select the role you would like to change to.", true);
        SubcommandData ms_pingmods = new SubcommandData("ping", "Modify whether you want to ping mods on alert or not");
        ms_pingmods.addOption(OptionType.BOOLEAN, "pingmods", "True/False", true);
        SubcommandData ms_autoscan = new SubcommandData("autoscan", "Modify whether you want to autoscan users or not");
        ms_autoscan.addOption(OptionType.BOOLEAN, "scan", "True/False", true);
        modifySettings.addSubcommands(ms_autoscan, ms_pingmods, ms_modrole, ms_channel);
        modifySettings.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        return modifySettings;
    }

}