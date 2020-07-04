package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
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
    public void run(User author, MessageChannel channel, String args, Guild guild) {
        if (!guild.getMember(author).hasPermission(Permission.MANAGE_SERVER)) {
            MessagingUtils.sendNoPermissions(channel, "Manage Server");
            return;
        }
        if (args == null || args.trim().isEmpty()) {
            MessagingUtils.sendError(channel);
            return;
        }
        ModuleManager.getInstanceByGuildId(guild.getId()).setPrefix(args);
        MessagingUtils.sendCompleted(channel);
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
