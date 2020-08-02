package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import to.us.awesomest.aphelia.data.CoinData;
import to.us.awesomest.aphelia.module.MessagingUtils;

import java.awt.*;

public class GiveCoins implements Command {
    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public void run(User author, MessageChannel channel, String args, Guild guild) {
        //noinspection ConstantConditions
        if(!guild.getMember(author).hasPermission(Permission.MANAGE_SERVER)) {
            MessagingUtils.sendNoPermissions(channel, "Manage Server");
            return;
        }
        try {
            String targetId = author.getId();
            int amount;
            if (args.contains(" ")) { //check if there's more than 1 argument.
                try {
                    targetId = CommandUtils.parseUser(guild, args.split(" ")[0]).getId();
                    amount = Integer.parseInt(args.split(" ")[1]);
                } catch (IllegalArgumentException e) {
                    MessagingUtils.sendError(channel, "That formatting seems to be wrong. Check that the user exists and the amount is a valid integer.");
                    return;
                }
            } else {
                amount = Integer.parseInt(args);
            }
            String bal = String.valueOf(Integer.parseInt(CoinData.getInstanceByGuildId(guild.getId()).getEntry(targetId)) + amount);
            CoinData.getInstanceByGuildId(guild.getId()).setEntry(targetId, bal);
            EmbedBuilder balanceInfoBuilder = new EmbedBuilder();
            balanceInfoBuilder
                    .setTitle("Balance")
                    .setColor(new Color(127, 255, 0))
                    .setDescription(bal);
            channel.sendMessage(balanceInfoBuilder.build()).queue();
        }
        catch(Exception e) {
            e.printStackTrace();
            MessagingUtils.sendError(channel, "Usage: !giveCoins [user] <amount>");
        }

    }

    @Override
    public String getName() {
        return "giveCoins";
    }

    @Override
    public String getDescription() {
        return "Give coins to a user. Usage: giveCoins <amount> [user]";
    }
}
