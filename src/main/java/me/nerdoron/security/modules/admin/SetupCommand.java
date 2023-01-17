package me.nerdoron.security.modules.admin;

import me.nerdoron.security.Global;
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

public class SetupCommand extends SlashCommand {
    SettingsSQL settingsSQL = new SettingsSQL();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!(event.getMember().hasPermission(Permission.ADMINISTRATOR)))
            return;
        String guildId = event.getGuild().getId();
        if (settingsSQL.isGuildSetup(guildId)) {
            event.reply("The guild is already setup! To change a setting use `/modifysetting [setting]`")
                    .setEphemeral(true).queue();
            return;
        }

        // alerts channel
        Channel channel = event.getInteraction().getOption("alertchannel").getAsChannel();
        if (!(channel.getType().equals(ChannelType.TEXT))) {
            event.reply(
                    "ERROR; The Alerts Channel must be a Text Channel. Please re-run the command and select a correct channel.")
                    .queue();
            return;
        }
        String alertsChannelId = channel.getId();

        // mod role
        Role role = event.getInteraction().getOption("modrole").getAsRole();
        String modRoleId = role.getId();

        // ping mods
        Boolean pingMods = event.getInteraction().getOption("pingmodsonalert").getAsBoolean();

        // autoScan

        Boolean autoScan = event.getInteraction().getOption("autoscan").getAsBoolean();

        settingsSQL.setupGuild(guildId, alertsChannelId, modRoleId, pingMods, autoScan);
        MessageEmbed settingsEmbed = new EmbedBuilder()
                .setTitle("Guild Settings")
                .setThumbnail(event.getGuild().getIconUrl())
                .setDescription("Settings for guild `" + event.getGuild().getName() + "`:")
                .addField("Alerts Channel", channel.getAsMention(), false)
                .addField("Mod Role", role.getAsMention(), false)
                .addField("Ping Mods on Alert", String.valueOf(pingMods), false)
                .addField("Auto-Scan", String.valueOf(autoScan), false)
                .setColor(Global.embedColor)
                .build();

        event.replyEmbeds(settingsEmbed).queue();
    }

    @Override
    public SlashCommandData getSlash() {
        SlashCommandData setup = Commands.slash("setup", "Setup OneWay Gate.");
        setup.addOption(OptionType.CHANNEL, "alertchannel", "The channels where OneWay alerts will be shown.",
                true);
        setup.addOption(OptionType.ROLE, "modrole", "The Moderator role in your server.", true);
        setup.addOption(OptionType.BOOLEAN, "pingmodsonalert",
                "Do you want to ping moderators when an alerts comes?",
                true);
        setup.addOption(OptionType.BOOLEAN, "autoscan",
                "Would you like automatically scan all users in your server?",
                true);
        setup.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        return setup;
    }

}
