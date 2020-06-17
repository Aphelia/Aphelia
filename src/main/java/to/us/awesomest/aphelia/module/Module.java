package to.us.awesomest.aphelia.module;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public interface Module {
    void run(User author, MessageChannel channel, String args, Guild guild);
    String getName();
    String getDescription();
}
