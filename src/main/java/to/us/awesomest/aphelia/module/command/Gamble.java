package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import to.us.awesomest.aphelia.data.CoinData;
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
    public void run(User author, MessageChannel channel, String args, Guild guild) {
        String bal;

        try {
            if (Integer.parseInt(CoinData.getInstanceByGuildId(guild.getId()).getEntry(author.getId())) < Integer.parseInt(args)) {
                EmbedBuilder feedbackBuilder = new EmbedBuilder();
                feedbackBuilder
                        .setTitle("Not enough money!")
                        .setColor(new Color(0, 255, 0))
                        .setDescription("You can't gamble more than you're worth!");
                channel.sendMessage(feedbackBuilder.build()).queue();
                return;
            }
            if (Integer.parseInt(args) <= 0) {
                EmbedBuilder feedbackBuilder = new EmbedBuilder();
                feedbackBuilder
                        .setTitle("Positives only.")
                        .setColor(new Color(0, 255, 0))
                        .setDescription("Don't be so negative. Or worthless.");
                channel.sendMessage(feedbackBuilder.build()).queue();
                return;
            }
            if (random.nextBoolean()) {
                bal = String.valueOf(Integer.parseInt(CoinData.getInstanceByGuildId(guild.getId()).getEntry(author.getId())) + Integer.parseInt(args));
                EmbedBuilder balanceInfoBuilder = new EmbedBuilder();
                balanceInfoBuilder
                        .setTitle("You won!")
                        .setColor(new Color(0, 255, 0))
                        .addField("Your balance:", bal, false);
                channel.sendMessage(balanceInfoBuilder.build()).queue();
            }
            else {
                bal = String.valueOf(Integer.parseInt(CoinData.getInstanceByGuildId(guild.getId()).getEntry(author.getId())) - Integer.parseInt(args));
                EmbedBuilder balanceInfoBuilder = new EmbedBuilder();
                balanceInfoBuilder
                        .setTitle("You lost!")
                        .setColor(new Color(255, 0, 0))
                        .addField("Your balance:", bal, false);
                channel.sendMessage(balanceInfoBuilder.build()).queue();
            }
            CoinData.getInstanceByGuildId(guild.getId()).setEntry(author.getId(), bal);
        }
        catch(Exception e) {
            MessagingUtils.sendError(channel);
        }
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
