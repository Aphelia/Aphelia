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
    long processorThreadCount = 0;
    @SubscribeEvent
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        new Thread(new Processor(event), "ChatListener Processor " + ++processorThreadCount).start();
    }

    private static class Processor implements Runnable {
        MessageReceivedEvent event;
        private Processor(MessageReceivedEvent event) {
            this.event = event;
        }
        @Override
        public void run() {
            if(event.getAuthor().isBot()) return;

            LoggerFactory.getLogger("ChatListener Processor").debug("Processing message " + event.getMessage().getContentRaw());
            if(event.isFromGuild()) {
                if(URLFilterHandler.getURLFilterHandlerByGuildId(event.getGuild().getId()).run(event.getMessage())) return;
                if(ModuleManager.getInstanceByGuildId(event.getGuild().getId()).runCommands(event.getMessage())) return;
                LoggerFactory.getLogger("ChatListener Processor").debug("Did not find relevant command for message " + event.getMessage().getContentRaw() + ", going to shortcuts.");
                ShortcutHandler.getShortcutHandlerByGuildId(event.getGuild().getId()).run(event.getMessage());
            } else if(ModuleManager.getDMInstance().runCommands(event.getMessage())) return;
            LoggerFactory.getLogger("ChatListener Processor").debug("Sending to MC " + event.getMessage().getContentRaw());
            MCChannelHandler.getInstance().run(event.getMessage());
        }
    }
}
