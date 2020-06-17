package to.us.awesomest.aphelia.module.chathandlers;

import org.slf4j.LoggerFactory;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import to.us.awesomest.aphelia.comlink.Satellite;

public class MCChannelHandler implements ChatHandler {
    private static MCChannelHandler instance;
    private MCChannelHandler(){}

    public static MCChannelHandler getInstance(){
        if(instance == null) instance = new MCChannelHandler();
        return instance;
    }
    @Override
    public boolean adminException() {
        return false;
    }

    @Override
    public void run(User author, MessageChannel channel, String message, @NotNull Guild guild) {
        if(!Satellite.hasOutputStream(channel.getId())) {
            LoggerFactory.getLogger("MCChannelHandler").debug("Skipped message \"" + message + "\" as no matching client was found.");
            return;
        }
        Satellite.passChatMessage(channel.getId(), author.getName(), message);
        LoggerFactory.getLogger("MCChannelHandler").debug("Sent message \"" + message + "\" to Satellite.");
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
