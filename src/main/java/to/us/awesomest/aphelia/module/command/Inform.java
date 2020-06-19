package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Inform implements Command {
    @Override
    public boolean isDMUsable() {
        return true;
    }

    @Override
    public void run(User author, MessageChannel channel, String args, Guild guild) {


    }

    @Override
    public String getName() {
        return "inform";
    }

    @Override
    public String getDescription() {
        return "Inform users in an embed-esque way. Even though embeds can usually only be used by bots, Aphelia allows humans to use it too!";
    }
}
