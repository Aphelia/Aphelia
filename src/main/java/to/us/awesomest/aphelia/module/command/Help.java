package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import to.us.awesomest.aphelia.Aphelia;
import to.us.awesomest.aphelia.data.ShortcutData;
import to.us.awesomest.aphelia.globalutils.LanguageUtils;
import to.us.awesomest.aphelia.module.ModuleManager;

import java.awt.*;
import java.util.Map;

public class Help implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public boolean run(Message message) {
        MessageChannel channel = message.getChannel();
        Guild guild = message.getGuild();
        EmbedBuilder commandListBuilder = new EmbedBuilder();
        commandListBuilder
                .setColor(new Color(255,215,0))
                .setTitle(LanguageUtils.getMessage(message.getGuild(), "infoCommands"))
                .setDescription(LanguageUtils.getMessage(message.getGuild(), "infoCommandsLong"))
                .setFooter("Aphelia " + Aphelia.getVersion() + " " + LanguageUtils.getMessage(message.getGuild(), "phraseMadeBy") + " AwesomestGamer#4703");
        for(Command command : ModuleManager.getInstanceByGuildId(guild.getId()).getEnabledCommands()) {
            commandListBuilder.addField(ModuleManager.getInstanceByGuildId(guild.getId()).getPrefix() + command.getName(), command.getDescription(),false);
        }
        commandListBuilder.addField("", LanguageUtils.getMessage(message.getGuild(), "adCommandSet"), false);
        channel.sendMessage(commandListBuilder.build()).queue();
        EmbedBuilder shortcutListBuilder = new EmbedBuilder();
        shortcutListBuilder
                .setColor(new Color(255,215,0))
                .setTitle(LanguageUtils.getMessage(message.getGuild(), "phraseShortcuts"))
                .setDescription(LanguageUtils.getMessage(message.getGuild(), "infoShortcutsLong"))
                .setFooter("Aphelia " + Aphelia.getVersion() + " by AwesomestGamer#4703");
        for(Map.Entry<String, String> shortcut : ShortcutData.getInstanceByGuildId(guild.getId()).getAllEntries()) {
            shortcutListBuilder.addField(shortcut.getKey(), shortcut.getValue(),false);
        }
        if(shortcutListBuilder.getFields().isEmpty()) shortcutListBuilder.addField("", LanguageUtils.getMessage(message.getGuild(), "infoDefineShortcuts"), false);
        shortcutListBuilder.addField("", LanguageUtils.getMessage(message.getGuild(), "adShortcutFeature"), false);
        channel.sendMessage(shortcutListBuilder.build()).queue();
        return true;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "List the available commands.";
    }
}