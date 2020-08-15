package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.entities.Message;

public class CheckComs implements Command {
    //Implement singleton handling
    static CheckComs instance = null;

    @Override
    public void run(Message message) {
        message.getChannel().sendMessage("ComChecks complete, standby for launch.").queue();
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
