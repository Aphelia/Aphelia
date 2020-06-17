package to.us.awesomest.mybit.module.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import to.us.awesomest.mybit.module.MessagingUtils;
import to.us.awesomest.mybit.module.ModuleManager;

public class ChangePrefix implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public void run(User author, MessageChannel channel, String args, Guild guild) {
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
