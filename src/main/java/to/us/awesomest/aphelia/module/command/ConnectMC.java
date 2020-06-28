package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import to.us.awesomest.aphelia.data.MCData;
import to.us.awesomest.aphelia.module.MessagingUtils;

import java.awt.*;
import java.util.Objects;

public class ConnectMC implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public void run(User author, MessageChannel channel, String args, @NotNull Guild guild) {
        if (channel.getType().isGuild() && !Objects.requireNonNull(guild.getMember(author)).hasPermission(Permission.MANAGE_SERVER)) {
            MessagingUtils.sendNoPermissions(channel, "Manage Server");
            return;
        }
        if (args.trim().toLowerCase().contains("connectmc")) {
            EmbedBuilder commandListBuilder = new EmbedBuilder();
            commandListBuilder
                    .setColor(new Color(255, 0, 0))
                    .setTitle("Invalid Syntax!")
                    .setDescription("Usage: !connectMC <token>");
            channel.sendMessage(commandListBuilder.build()).queue();
            return;
        }
        LoggerFactory.getLogger("ConnectMC").debug("Added token " + args + " to channel " + channel.getId());
        MCData.getInstance().setEntry(args, channel.getId());
        EmbedBuilder feedbackBuilder = new EmbedBuilder();
        feedbackBuilder
                .setColor(new Color(0, 255, 0))
                .setTitle("Token set!")
                .setDescription("Please remember to delete both this message and your command afterwards, as a third-party could use your token to take control over your server.");
        channel.sendMessage(feedbackBuilder.build()).queue();
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
