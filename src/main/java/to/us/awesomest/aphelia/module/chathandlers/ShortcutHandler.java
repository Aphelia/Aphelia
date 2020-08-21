package to.us.awesomest.aphelia.module.chathandlers;

import net.dv8tion.jda.api.entities.Message;
import org.slf4j.LoggerFactory;
import to.us.awesomest.aphelia.data.ShortcutData;

import java.util.HashMap;

public class ShortcutHandler implements ChatHandler{
    private static final HashMap<String, ShortcutHandler> guildIdShortcutHandlerMap = new HashMap<>();

    public static ShortcutHandler getShortcutHandlerByGuildId(String guildId) {
        if(!guildIdShortcutHandlerMap.containsKey(guildId)) guildIdShortcutHandlerMap.put(guildId, new ShortcutHandler());
        return guildIdShortcutHandlerMap.get(guildId);
    }

    @Override
    public boolean run(Message message) {
        if(ShortcutData.getInstanceByGuildId(message.getGuild().getId()).hasEntry(message.getContentRaw().toLowerCase()))
        message.getChannel().sendMessage(ShortcutData.getInstanceByGuildId(message.getGuild().getId()).getEntry(message.getContentRaw().toLowerCase())).queue();
        else LoggerFactory.getLogger("ShortcutHandler").info("No valid shortcut found message " + message.getContentRaw());
        return true;
    }

    @Override
    public boolean adminException() {
        return false;
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
