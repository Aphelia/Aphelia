package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import to.us.awesomest.aphelia.data.TicketData;
import to.us.awesomest.aphelia.globalutils.LanguageUtils;
import to.us.awesomest.aphelia.module.MessagingUtils;
import to.us.awesomest.aphelia.module.ModuleManager;

import java.util.EnumSet;

public class Ticket implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public boolean run(Message message) {
        MessagingUtils.sendError(message.getChannel(), "This command has temporarily been globally disabled.");
        if(true) return true; //add if true to prevent unreachable checks
        User author = message.getAuthor();
        MessageChannel channel = message.getChannel();
        String args = CommandUtils.getArgs(message.getContentRaw());
        Guild guild = message.getGuild();
        Member member = guild.getMember(author);
        assert member != null;
        if (TicketData.getInstanceByGuildId(guild.getId()).hasValue(author.getId())) {
            MessagingUtils.sendError(channel, LanguageUtils.getMessage(message.getGuild(), "errorTicketAlreadyOpen"));
            return false;
        }
        EnumSet<Permission> read = EnumSet.of(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE);
        TextChannel ticketChannel;
        if (guild.getTextChannelsByName("base-ticket", true).size() != 1) {
            if(guild.getRolesByName("Support", true).size() > 0) {
                ticketChannel = guild
                        .createTextChannel(author.getName() + "'s ticket")
                        .addMemberPermissionOverride(author.getIdLong(), read, null)
                        .addRolePermissionOverride(guild.getRolesByName("Support", true).get(0).getIdLong(), read, null)
                        .addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, read)
                        .complete();
            } else if(guild.getRolesByName("Staff", true).size() > 0) {
                ticketChannel = guild
                        .createTextChannel(author.getName() + "'s ticket")
                        .addMemberPermissionOverride(author.getIdLong(), read, null)
                        .addRolePermissionOverride(guild.getRolesByName("Staff", true).get(0).getIdLong(), read, null)
                        .addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, read)
                        .complete();
            } else {
                ticketChannel = guild
                        .createTextChannel(author.getName() + "'s ticket")
                        .addMemberPermissionOverride(author.getIdLong(), read, null)
                        .complete();
            }
            MessagingUtils.sendInfo(ticketChannel, LanguageUtils.getMessage(message.getGuild(), "infoNoBaseTicket"));
        } else {
            ticketChannel = guild.createCopyOfChannel(guild.getTextChannelsByName("base-ticket", true).get(0))
                    .setName(author.getName() + "'s ticket")
                    .addMemberPermissionOverride(author.getIdLong(), read, null)
                    .complete();
        }
        ticketChannel.sendMessage(LanguageUtils.getMessage(message.getGuild(), "infoCloseTicket").replace("%prefix%", ModuleManager.getInstanceByGuildId(guild.getId()).getPrefix())).queue();
        TicketData.getInstanceByGuildId(guild.getId()).setEntry(ticketChannel.getId(), author.getId());
        MessagingUtils.sendCompleted(channel);
        return true;
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
