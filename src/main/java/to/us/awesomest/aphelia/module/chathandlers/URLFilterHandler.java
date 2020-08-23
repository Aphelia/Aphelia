package to.us.awesomest.aphelia.module.chathandlers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public boolean run(Message message) {
        @SuppressWarnings("ConstantConditions")
        boolean hasAdBypass = (message.getMember().getRoles().stream()
                .filter(role -> role.getName().equals("Aphelia AdBypass")) // filter by role name
                .count()) >= 1; //probably a cleaner way to do this but I don't
        if(hasAdBypass) return false;
        Pattern pattern = Pattern.compile("(?:[a-zA-Z0-9]+(?:\\.|\\[dot\\]|\\[DOT]|,))+(?<tld>[a-zA-Z]{2,20})");

        Matcher matcher = pattern.matcher(message.getContentRaw());
        if(matcher.find(0) && TLDUtils.isTLD(matcher.group("tld"))) {
            MessageChannel channel = message.getChannel();
            message.delete().queue();
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
            return true;
        }
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
