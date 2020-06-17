package to.us.awesomest.mybit.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.slf4j.LoggerFactory;
import to.us.awesomest.mybit.module.chathandlers.MCChannelHandler;
import to.us.awesomest.mybit.module.chathandlers.ShortcutHandler;
import to.us.awesomest.mybit.module.ModuleManager;


public class ChatListener extends ListenerAdapter {
    @SubscribeEvent
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        ModuleManager manager;
        Guild guild = null;
        if(event.isFromGuild()) {
            manager = ModuleManager.getInstanceByGuildId(event.getGuild().getId());
            guild = event.getGuild();
        }
        else manager = ModuleManager.getDMInstance();

        if(manager.runCommands(
                event.getAuthor(),
                event.getChannel(),
                event.getMessage().getContentRaw(),
                guild)) return;
        LoggerFactory.getLogger("ChatListener").debug("Did not find relevant command for message " + event.getMessage().getContentRaw() + ", going to shortcuts.");
        ShortcutHandler.getShortcutHandlerByGuildId(event.getGuild().getId()).run(
                event.getAuthor(),
                event.getChannel(),
                event.getMessage().getContentRaw(),
                guild);
        LoggerFactory.getLogger("ChatListener").debug("Sending to MC " + event.getMessage().getContentRaw());
        MCChannelHandler.getInstance().run(
                event.getAuthor(),
                event.getChannel(),
                event.getMessage().getContentRaw(),
                guild);
        LoggerFactory.getLogger("ChatListener").debug("Processed message " + event.getMessage().getContentRaw());
    }
}
