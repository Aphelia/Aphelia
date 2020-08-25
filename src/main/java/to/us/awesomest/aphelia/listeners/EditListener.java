package to.us.awesomest.aphelia.listeners;

import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.slf4j.LoggerFactory;
import to.us.awesomest.aphelia.module.chathandlers.URLFilterHandler;

public class EditListener extends ListenerAdapter {
    long processorThreadCount = 0;
    @SubscribeEvent
    public void onMessageUpdateEvent(MessageUpdateEvent event) {
        new Thread(new EditListener.Processor(event), "EditListener Processor " + ++processorThreadCount).start();
    }

    private static class Processor implements Runnable {
        MessageUpdateEvent event;
        private Processor(MessageUpdateEvent event) {
            this.event = event;
        }
        @Override
        public void run() {
            if(event.getAuthor().isBot()) return;
            LoggerFactory.getLogger("EditListener Processor").debug("Processing message " + event.getMessage().getContentRaw());
            if(event.isFromGuild()) {
                URLFilterHandler.getURLFilterHandlerByGuildId(event.getGuild().getId()).run(event.getMessage());
            }
        }
    }
}
