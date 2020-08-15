package to.us.awesomest.aphelia.module.chathandlers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class URLFilterHandler implements ChatHandler {

    private static final HashMap<String, URLFilterHandler> guildIdURLFilterHandlerMap = new HashMap<>();

    public static URLFilterHandler getURLFilterHandlerByGuildId(String guildId) {
        if(!guildIdURLFilterHandlerMap.containsKey(guildId)) guildIdURLFilterHandlerMap.put(guildId, new URLFilterHandler());
        return guildIdURLFilterHandlerMap.get(guildId);
    }

    @Override
    public boolean adminException() {
        return true; //TODO currently does nothing but will fix later
    }

    @Override
    public void run(Message message) {
        @SuppressWarnings("ConstantConditions")
        boolean hasAdBypass = (message.getMember().getRoles().stream()
                .filter(role -> role.getName().equals("Aphelia AdBypass")) // filter by role name
                .count()) >= 1; //probably a cleaner way to do this but I don't
        if(hasAdBypass) return;
        if(message.getContentRaw().matches(".*[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*).*")) {
            MessageChannel channel = message.getChannel();
            message.delete().complete(); //I do want it to block.
            EmbedBuilder feedbackBuilder = new EmbedBuilder();
            feedbackBuilder.setTitle("Please don't advertise here!");
            feedbackBuilder.setColor(Color.YELLOW);
            Message sent = channel.sendMessage(feedbackBuilder.build()).complete();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sent.delete().queue();
        }
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
