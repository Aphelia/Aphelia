package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import to.us.awesomest.aphelia.Aphelia;
import to.us.awesomest.aphelia.data.ShortcutData;
import to.us.awesomest.aphelia.module.ModuleManager;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Map;

public class Help implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public void run(User author, MessageChannel channel, @Nullable String args, @NotNull Guild guild) {
        EmbedBuilder commandListBuilder = new EmbedBuilder();
        commandListBuilder
                .setColor(new Color(255,215,0))
                .setTitle("Commands")
                .setDescription("The official commands for Aphelia.")
                .setFooter("Aphelia " + Aphelia.getVersion() + " by AwesomestGamer#4703");
        for(Command command : ModuleManager.getInstanceByGuildId(guild.getId()).getEnabledCommands()) {
            commandListBuilder.addField(ModuleManager.getInstanceByGuildId(guild.getId()).getPrefix() + command.getName(), command.getDescription(),false);
        }
        channel.sendMessage(commandListBuilder.build()).queue();
        EmbedBuilder shortcutListBuilder = new EmbedBuilder();
        shortcutListBuilder
                .setColor(new Color(255,215,0))
                .setTitle("Shortcuts")
                .setDescription("The user defined shortcuts for this server.")
                .setFooter("Aphelia " + Aphelia.getVersion() + " by AwesomestGamer#4703");
        for(Map.Entry<String, String> shortcut : ShortcutData.getInstanceByGuildId(guild.getId()).getAllEntries()) {
            shortcutListBuilder.addField(shortcut.getKey(), shortcut.getValue(),false);
        }
        if(shortcutListBuilder.getFields().isEmpty()) shortcutListBuilder.addField("", "*No shortcuts have been defined. Go define some (or, if you don't have permissions, ask a manager to do it for you)!*", false);
        channel.sendMessage(shortcutListBuilder.build()).queue();
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