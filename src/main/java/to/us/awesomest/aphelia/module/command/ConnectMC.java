package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.slf4j.LoggerFactory;
import to.us.awesomest.aphelia.data.MCData;
import to.us.awesomest.aphelia.globalutils.LanguageUtils;
import to.us.awesomest.aphelia.module.MessagingUtils;

import java.awt.*;

public class ConnectMC implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public boolean run(Message message) {
        MessageChannel channel = message.getChannel();
        String args = CommandUtils.getArgs(message.getContentRaw());
        //noinspection ConstantConditions
        if (channel.getType().isGuild() && !message.getGuild().getMember(message.getAuthor()).hasPermission(Permission.MANAGE_SERVER)) {
            MessagingUtils.sendNoPermissions(channel, LanguageUtils.getMessage(message.getGuild(), "permissionManageServer"));
            return false;
        }
        if (args.trim().isEmpty()) {
            EmbedBuilder commandListBuilder = new EmbedBuilder();
            commandListBuilder
                    .setColor(new Color(255, 0, 0))
                    .setTitle(LanguageUtils.getMessage(message.getGuild(), "errorInvalidSyntax"))
                    .setDescription("Usage: !connectMC <token>");
            channel.sendMessage(commandListBuilder.build()).queue();
            return false;
        }
        LoggerFactory.getLogger("ConnectMC").debug("Added token " + args + " to channel " + channel.getId());
        MCData.getInstance().setEntry(args, channel.getId());
        message.delete().queue();
        EmbedBuilder feedbackBuilder = new EmbedBuilder();
        feedbackBuilder
                .setColor(new Color(0, 255, 0))
                .setTitle(LanguageUtils.getMessage(message.getGuild(), "successTokenSet"))
                .setDescription(LanguageUtils.getMessage(message.getGuild(), "successMessageAutoDeleted"));
        channel.sendMessage(feedbackBuilder.build()).queue();
        return true;
    }

    @Override
    public String getName() {
        return "connectMC";
    }

    @Override
    public String getDescription() {
        return "Connect your Minecraft Server to Aphelia.";
    }
}
