package to.us.awesomest.mybit.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import to.us.awesomest.mybit.data.CoinData;
import to.us.awesomest.mybit.module.MessagingUtils;

import java.awt.*;

public class TakeCoins implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public void run(User author, MessageChannel channel, String args, Guild guild) {
        if(!guild.getMember(author).hasPermission(Permission.MANAGE_SERVER)) {
            MessagingUtils.sendNoPermissions(channel, "Manage Server");
            return;
        }
        try {
            String bal = String.valueOf(Integer.valueOf(CoinData.getInstanceByGuildId(guild.getId()).getEntry(author.getId())) - Integer.valueOf(args));
            CoinData.getInstanceByGuildId(guild.getId()).setEntry(author.getId(), bal);
            EmbedBuilder balanceInfoBuilder = new EmbedBuilder();
            balanceInfoBuilder
                    .setTitle("Balance")
                    .setColor(new Color(127, 255, 0))
                    .addField("Your balance:", bal, false);
            channel.sendMessage(balanceInfoBuilder.build()).queue();
        }
        catch(Exception e) {
            MessagingUtils.sendError(channel);
        }
    }

    @Override
    public String getName() {
        return "takeCoins";
    }

    @Override
    public String getDescription() {
        return "Remove coins from a user.";
    }
}
