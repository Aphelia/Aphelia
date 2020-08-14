package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;
import to.us.awesomest.aphelia.Aphelia;
import to.us.awesomest.aphelia.module.MessagingUtils;

import java.awt.*;
import java.util.EnumSet;
import java.util.List;

public class Suggest implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void run(Message message) {
        User author = message.getAuthor();
        MessageChannel channel = message.getChannel();
        String args = CommandUtils.getArgs(message.getContentRaw());
        Guild guild = message.getGuild();
        EnumSet<Permission> deny = EnumSet.of(Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_WRITE);
        List<TextChannel> suggestionChannels = guild.getTextChannelsByName("suggestions", true);
        if (suggestionChannels.size() < 1) {
            MessagingUtils.sendError(channel, "There is no channel called #suggestions! Please create one.");
            return;
        }
        if (args == null || args.trim().isEmpty()) {
            MessagingUtils.sendError(channel, "You must supply a suggestion! Usage: !suggest [name]");
            return;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setColor(new Color(0, 255, 0))
                .setAuthor(guild.getMember(author).getEffectiveName(), null, author.getEffectiveAvatarUrl())
                .setTitle("Suggestion")
                .setDescription(args);
        suggestionChannels.get(0).putPermissionOverride(guild.getMember(Aphelia.bot.getSelfUser())).setAllow(deny).queue();
        suggestionChannels.get(0).putPermissionOverride(guild.getPublicRole()).deny(deny).queue();
        Message suggestion = suggestionChannels.get(0).sendMessage(embedBuilder.build()).complete();
        suggestion.addReaction("yes:726439200509001748").queue();
        suggestion.addReaction("no:726439200450412584").queue();
        MessagingUtils.sendCompleted(channel);
    }

    @Override
    public String getName() {
        return "suggest";
    }

    @Override
    public String getDescription() {
        return "Create a suggestion.";
    }
}
