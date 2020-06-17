package to.us.awesomest.mybit.module.chathandlers;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Assistant implements ChatHandler{
    @Override
    public boolean adminException() {
        return false;
    }

    @Override
    public void run(User author, MessageChannel channel, String args, Guild guild) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
