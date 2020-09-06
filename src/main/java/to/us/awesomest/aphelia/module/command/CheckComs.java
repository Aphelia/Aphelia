package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.entities.Message;
import to.us.awesomest.aphelia.globalutils.LanguageUtils;

public class CheckComs implements Command {
    //Implement singleton handling
    static CheckComs instance = null;

    @Override
    public boolean run(Message message) {
        message.getChannel().sendMessage(LanguageUtils.getMessage(message.getGuild(), "comChecksSuccess")
                .replace("%gatewayping%", String.valueOf(message.getJDA().getGatewayPing()))
                .replace("%restping%", String.valueOf(message.getJDA().getGatewayPing())))
                .queue();
        return true;
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
