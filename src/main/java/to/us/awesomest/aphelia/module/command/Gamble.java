package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import to.us.awesomest.aphelia.data.CoinData;
import to.us.awesomest.aphelia.globalutils.LanguageUtils;
import to.us.awesomest.aphelia.module.MessagingUtils;

import java.awt.*;
import java.util.Random;

public class Gamble implements Command {
    Random random = new Random();
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public boolean run(Message message) {
        User author = message.getAuthor();
        MessageChannel channel = message.getChannel();
        String args = CommandUtils.getArgs(message.getContentRaw());
        Guild guild = message.getGuild();
        String bal;

        try {
            if (Integer.parseInt(CoinData.getInstanceByGuildId(guild.getId()).getEntry(author.getId())) < Integer.parseInt(args)) {
                EmbedBuilder feedbackBuilder = new EmbedBuilder();
                feedbackBuilder
                        .setTitle(LanguageUtils.getMessage(message.getGuild(), "errorMissingCoins"))
                        .setColor(new Color(0, 255, 0))
                        .setDescription(LanguageUtils.getMessage(message.getGuild(), "errorMissingCoinsLong"));
                channel.sendMessage(feedbackBuilder.build()).queue();
                return false;
            }
            if (Integer.parseInt(args) <= 0) {
                EmbedBuilder feedbackBuilder = new EmbedBuilder();
                feedbackBuilder
                        .setTitle(LanguageUtils.getMessage(message.getGuild(), "errorPositivesOnly"))
                        .setColor(new Color(0, 255, 0))
                        .setDescription(LanguageUtils.getMessage(message.getGuild(), "errorPositivesOnlyLong"));
                channel.sendMessage(feedbackBuilder.build()).queue();
                return false;
            }
            if (random.nextBoolean()) {
                bal = String.valueOf(Integer.parseInt(CoinData.getInstanceByGuildId(guild.getId()).getEntry(author.getId())) + Integer.parseInt(args));
                EmbedBuilder balanceInfoBuilder = new EmbedBuilder();
                balanceInfoBuilder
                        .setTitle(LanguageUtils.getMessage(message.getGuild(), "infoYouWon"))
                        .setColor(new Color(0, 255, 0))
                        .addField(LanguageUtils.getMessage(message.getGuild(), "infoYourBalance") + ":", bal, false);
                channel.sendMessage(balanceInfoBuilder.build()).queue();
            }
            else {
                bal = String.valueOf(Integer.parseInt(CoinData.getInstanceByGuildId(guild.getId()).getEntry(author.getId())) - Integer.parseInt(args));
                EmbedBuilder balanceInfoBuilder = new EmbedBuilder();
                balanceInfoBuilder
                        .setTitle(LanguageUtils.getMessage(message.getGuild(), "infoYouLost"))
                        .setColor(new Color(255, 0, 0))
                        .addField(LanguageUtils.getMessage(message.getGuild(), "infoYourBalance") + ":", bal, false);
                channel.sendMessage(balanceInfoBuilder.build()).queue();
            }
            CoinData.getInstanceByGuildId(guild.getId()).setEntry(author.getId(), bal);
        }
        catch(Exception e) {
            MessagingUtils.sendError(channel);
        }
        return true;
    }

    @Override
    public String getName() {
        return "gamble";
    }

    @Override
    public String getDescription() {
        return "Gamble your in-server coins. Is really just a test to see if randomness works. And to find best ways to enrage people.";
    }
}
