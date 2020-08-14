package to.us.awesomest.aphelia.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.slf4j.LoggerFactory;
import to.us.awesomest.aphelia.module.ModuleManager;
import to.us.awesomest.aphelia.module.chathandlers.MCChannelHandler;
import to.us.awesomest.aphelia.module.chathandlers.ShortcutHandler;
import to.us.awesomest.aphelia.module.chathandlers.URLFilterHandler;


public class ChatListener extends ListenerAdapter {
    @SubscribeEvent
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;

        LoggerFactory.getLogger("ChatListener").debug("Processing message " + event.getMessage().getContentRaw());
        if(event.isFromGuild()) {
            if(ModuleManager.getInstanceByGuildId(event.getGuild().getId()).runCommands(event.getMessage())) return;
            URLFilterHandler.getURLFilterHandlerByGuildId(event.getGuild().getId()).run(event.getMessage());
            LoggerFactory.getLogger("ChatListener").debug("Did not find relevant command for message " + event.getMessage().getContentRaw() + ", going to shortcuts.");
            ShortcutHandler.getShortcutHandlerByGuildId(event.getGuild().getId()).run(event.getMessage());
            LoggerFactory.getLogger("ChatListener").debug("Sending to MC " + event.getMessage().getContentRaw());
        } else if(ModuleManager.getDMInstance().runCommands(event.getMessage())) return;
        MCChannelHandler.getInstance().run(event.getMessage());
    }
}
