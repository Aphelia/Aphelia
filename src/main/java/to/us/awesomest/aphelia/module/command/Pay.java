package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.LoggerFactory;
import to.us.awesomest.aphelia.data.CoinData;
import to.us.awesomest.aphelia.module.MessagingUtils;

public class Pay implements Command {

    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public void run(User author, MessageChannel channel, String args, Guild guild) {
        if (!args.contains(" ")) {
            MessagingUtils.sendError(channel, "Usage: !pay <user> <amount>");
            return;
        }
        String[] argsArray = {args.substring(0, args.lastIndexOf(" ")), args.substring(args.lastIndexOf(" ") + 1)};
        Member payee;
        try {
            payee = CommandUtils.parseUser(guild, argsArray[0]);
        } catch(IllegalArgumentException e) {
            MessagingUtils.sendError(channel, "Failed to find user " + argsArray[0]);
            return;
        }
        String amount = argsArray[1];
        LoggerFactory.getLogger("Transactions").debug("Amount: " + amount);
        int coins;
        try {
            coins = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            MessagingUtils.sendError(channel, "That's not a valid amount to pay.");
            return;
        }
        if(coins < 0) {
            MessagingUtils.sendError(channel, "You can't pay a negative amount!");
            return;
        }
        LoggerFactory.getLogger("Transactions").debug("User: " + payee);
        LoggerFactory.getLogger("Transactions").debug(String.valueOf(guild.getMemberByTag("BlockVerse#7009")));
        CoinData dataInstance = CoinData.getInstanceByGuildId(guild.getId());
        dataInstance.setEntry(author.getId(), Integer.toString(Integer.parseInt(dataInstance.getEntry(author.getId())) - coins));
        dataInstance.setEntry(payee.getId(), Integer.toString(Integer.parseInt(dataInstance.getEntry(payee.getId())) + coins));
        MessagingUtils.sendCompleted(channel, "You paid " + payee.getEffectiveName() + " " + coins + " coins.");
    }

    @Override
    public String getName() {
        return "pay";
    }

    @Override
    public String getDescription() {
        return "Pay someone.";
    }
}
