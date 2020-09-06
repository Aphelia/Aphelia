package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import to.us.awesomest.aphelia.data.CoinData;
import to.us.awesomest.aphelia.globalutils.LanguageUtils;
import to.us.awesomest.aphelia.module.MessagingUtils;

import java.awt.*;

public class TakeCoins implements Command {
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
        //noinspection ConstantConditions
        if(!guild.getMember(author).hasPermission(Permission.MANAGE_SERVER)) {
            MessagingUtils.sendNoPermissions(channel, LanguageUtils.getMessage(message.getGuild(), "permissionManageServer"));
            return false;
        }
        try {
            String targetId = author.getId();
            int amount;
            if (args.contains(" ")) { //check if there's more than 1 argument.
                try {
                    targetId = CommandUtils.parseUser(guild, args.split(" ")[0]).getId();
                    amount = Integer.parseInt(args.split(" ")[1]);
                } catch (IllegalArgumentException e) {
                    MessagingUtils.sendError(channel, LanguageUtils.getMessage(message.getGuild(), "errorInvalidBalanceLong"));
                    return false;
                }
            } else {
                amount = Integer.parseInt(args);
            }
            String bal = String.valueOf(Integer.parseInt(CoinData.getInstanceByGuildId(guild.getId()).getEntry(targetId)) - amount);
            CoinData.getInstanceByGuildId(guild.getId()).setEntry(targetId, bal);
            EmbedBuilder balanceInfoBuilder = new EmbedBuilder();
            balanceInfoBuilder
                    .setTitle(LanguageUtils.getMessage(message.getGuild(), "phraseBalanceCapitalized"))
                    .setColor(new Color(127, 255, 0))
                    .setDescription(bal);
            channel.sendMessage(balanceInfoBuilder.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
            MessagingUtils.sendError(channel, "Usage: !takeCoins [user] <amount>");
        }
        return true;

    }

    @Override
    public String getName() {
        return "takeCoins";
    }

    @Override
    public String getDescription() {
        return "Take coins from a user. Usage: takeCoins <amount> [user]";
    }
}
