package to.us.awesomest.mybit.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import to.us.awesomest.mybit.data.CoinData;

import java.awt.*;

public class Balance implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public void run(User author, MessageChannel channel, String args, Guild guild) {
        EmbedBuilder balanceInfoBuilder = new EmbedBuilder();
        balanceInfoBuilder
                .setTitle("Balance")
                .setColor(new Color(127, 255, 0))
                .addField("Your balance:", CoinData.getInstanceByGuildId(guild.getId()).getEntry(author.getId()), false);
        channel.sendMessage(balanceInfoBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Check your balance on this server.";
    }
}
