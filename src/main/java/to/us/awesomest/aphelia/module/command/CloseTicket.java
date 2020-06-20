package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.entities.*;
import to.us.awesomest.aphelia.data.TicketData;
import to.us.awesomest.aphelia.module.MessagingUtils;

public class CloseTicket implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public void run(User author, MessageChannel channel, String args, Guild guild) {
        Member member = guild.getMember(author);
        assert member != null;
        if (!TicketData.getInstanceByGuildId(guild.getId()).hasEntry(channel.getId())) {
            MessagingUtils.sendError(channel, "This channel is not a ticket!");
            return;
        }
        ((TextChannel) channel).delete().queue();
        TicketData.getInstanceByGuildId(guild.getId()).deleteEntry(channel.getId());
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
