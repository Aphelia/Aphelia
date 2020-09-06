package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.entities.*;
import org.slf4j.LoggerFactory;
import to.us.awesomest.aphelia.data.CoinData;
import to.us.awesomest.aphelia.globalutils.LanguageUtils;
import to.us.awesomest.aphelia.module.MessagingUtils;

public class Pay implements Command {

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
        if (!args.contains(" ")) {
            MessagingUtils.sendError(channel, "Usage: !pay <user> <amount>");
            return false;
        }
        String[] argsArray = {args.substring(0, args.lastIndexOf(" ")), args.substring(args.lastIndexOf(" ") + 1)};
        Member payee;
        try {
            payee = CommandUtils.parseUser(guild, argsArray[0]);
        } catch(IllegalArgumentException e) {
            MessagingUtils.sendError(channel, LanguageUtils.getMessage(message.getGuild(), "errorInvalidMember"));
            return false;
        }
        String amount = argsArray[1];
        LoggerFactory.getLogger("Transactions").debug("Amount: " + amount);
        int coins;
        try {
            coins = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            MessagingUtils.sendError(channel, LanguageUtils.getMessage(message.getGuild(), "errorInvalidPaymentAmount"));
            return false;
        }
        if(coins < 0) {
            MessagingUtils.sendError(channel, LanguageUtils.getMessage(message.getGuild(), "errorInvalidPaymentAmount"));
            return false;
        }
        LoggerFactory.getLogger("Transactions").debug("User: " + payee);
        LoggerFactory.getLogger("Transactions").debug(String.valueOf(guild.getMemberByTag("BlockVerse#7009")));
        CoinData dataInstance = CoinData.getInstanceByGuildId(guild.getId());
        dataInstance.setEntry(author.getId(), Integer.toString(Integer.parseInt(dataInstance.getEntry(author.getId())) - coins));
        dataInstance.setEntry(payee.getId(), Integer.toString(Integer.parseInt(dataInstance.getEntry(payee.getId())) + coins));
        MessagingUtils.sendCompleted(channel, LanguageUtils.getMessage(message.getGuild(), "phraseYouPaid") + " " + payee.getEffectiveName() + " " + coins + " coins.");
        return true;
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
