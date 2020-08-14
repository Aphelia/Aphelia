package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class CheckComs implements Command {
    //Implement singleton handling
    static CheckComs instance = null;

    @Override
    public void run(Message message) {
        MessageChannel channel = message.getChannel();
        channel.sendMessage("ComChecks complete, standby for launch.").queue();
    }

    @Override
    public String getName() {
        return "checkComs";
    }

    @Override
    public String getDescription() {
        return "Check if all of the communications with your shard are working. As if you were launching a rocket.";
    }

    @Override
    public boolean isDMUsable() {
        return true;
    }
}
