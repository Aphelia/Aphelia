package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.entities.*;
import to.us.awesomest.aphelia.data.TicketData;
import to.us.awesomest.aphelia.module.MessagingUtils;

public class Ticket implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public void run(User author, MessageChannel channel, String args, Guild guild) {
        Member member = guild.getMember(author);
        assert member != null;
        if (TicketData.getInstanceByGuildId(guild.getId()).hasValue(author.getId())) {
            MessagingUtils.sendError(channel, "You already have a ticket open!");
            return;
        }
        TextChannel ticketChannel = guild.createTextChannel(author.getName() + "'s ticket").complete();
        ticketChannel.sendMessage("*To close this ticket, do !close*").queue();
        TicketData.getInstanceByGuildId(guild.getId()).setEntry(ticketChannel.getId(), author.getId());
        MessagingUtils.sendCompleted(channel);
    }

    @Override
    public String getName() {
        return "ticket";
    }

    @Override
    public String getDescription() {
        return "Create a new ticket.";
    }
}
