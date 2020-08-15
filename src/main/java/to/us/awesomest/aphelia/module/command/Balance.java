package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import to.us.awesomest.aphelia.data.CoinData;
import to.us.awesomest.aphelia.module.MessagingUtils;

import java.awt.*;

public class Balance implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public void run(Message message) {
        User author = message.getAuthor();
        MessageChannel channel = message.getChannel();
        String args = CommandUtils.getArgs(message.getContentRaw());
        Guild guild = message.getGuild();
        EmbedBuilder balanceInfoBuilder = new EmbedBuilder();

        @SuppressWarnings("UnusedAssignment") String targetId = author.getId(); //not sure why the warning is happening in the first place?
        try {
            targetId = CommandUtils.parseUser(guild, args).getId();
        } catch (IllegalArgumentException e) {
            MessagingUtils.sendError(channel, "No such user is in this guild.");
            return;
        }

        balanceInfoBuilder
                .setTitle("Balance")
                .setColor(new Color(127, 255, 0))
                .addField("Balance:", CoinData.getInstanceByGuildId(guild.getId()).getEntry(targetId), false);
        channel.sendMessage(balanceInfoBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Check your balance on this server.";
    }
}
