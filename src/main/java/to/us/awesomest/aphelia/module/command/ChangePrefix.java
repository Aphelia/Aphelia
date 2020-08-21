package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import to.us.awesomest.aphelia.module.MessagingUtils;
import to.us.awesomest.aphelia.module.ModuleManager;

public class ChangePrefix implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean run(Message message) {
        User author = message.getAuthor();
        MessageChannel channel = message.getChannel();
        String args = CommandUtils.getArgs(message.getContentRaw());
        Guild guild = message.getGuild();
        if (!guild.getMember(author).hasPermission(Permission.MANAGE_SERVER)) {
            MessagingUtils.sendNoPermissions(channel, "Manage Server");
            return false;
        }
        if (args.trim().isEmpty()) {
            MessagingUtils.sendError(channel);
            return false;
        }
        ModuleManager.getInstanceByGuildId(guild.getId()).setPrefix(args);
        MessagingUtils.sendCompleted(channel);
        return true;
    }

    @Override
    public String getName() {
        return "changePrefix";
    }

    @Override
    public String getDescription() {
        return "Change the guild's command prefix.";
    }
}
