package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import to.us.awesomest.aphelia.data.TicketData;
import to.us.awesomest.aphelia.module.MessagingUtils;

public class CloseTicket implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public boolean run(Message message) {
        MessageChannel channel = message.getChannel();
        Member member = message.getGuild().getMember(message.getAuthor());
        assert member != null;
        if (!TicketData.getInstanceByGuildId(message.getGuild().getId()).hasEntry(channel.getId())) {
            MessagingUtils.sendError(channel, "This channel is not a ticket!");
            return false;
        }
        ((TextChannel) channel).delete().queue();
        TicketData.getInstanceByGuildId(message.getGuild().getId()).deleteEntry(channel.getId());
        return true;
    }

    @Override
    public String getName() {
        return "close";
    }

    @Override
    public String getDescription() {
        return "Delete the ticket you are currently in.";
    }
}
