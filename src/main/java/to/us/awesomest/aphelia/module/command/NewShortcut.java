package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import to.us.awesomest.aphelia.data.ShortcutData;
import to.us.awesomest.aphelia.module.MessagingUtils;

public class NewShortcut implements Command {

    @Override
    public String getName() {
        return "newShortcut";
    }

    @Override
    public String getDescription() {
        return "Create a new shortcut.";
    }

    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public void run(User author, MessageChannel channel, String args, Guild guild) {
        Member commander = guild.getMember(author);
        if(args == null) {
            MessagingUtils.sendError(channel, "Usage: !newShortcut <command case-insensitive> <output> (Note: Replace spaces with underscores).");
            return;
        }
        String[] commandArgsArray = args.split(" ");
        assert commander != null;
        if(!commander.hasPermission(Permission.MANAGE_SERVER)) {
            MessagingUtils.sendError(channel, "Not enough permissions! You must have **Manage Server**");
            return;
        }

        if(commandArgsArray.length != 2) {
            MessagingUtils.sendError(channel, "Usage: !newShortcut <command case-insensitive> <output> (Note: Replace spaces with underscores).");
            return;
        }

        String newTrigger = commandArgsArray[0].replaceAll("_", " ");
        String newOutput = commandArgsArray[1].replaceAll("_", " ");
        if(newOutput.trim().isEmpty()) {
            MessagingUtils.sendError(channel, "You can't have a shortcut with a blank output!");
            return;
        }
        if(newOutput.length() > 31 || commandArgsArray[0].length() > 31) {
            MessagingUtils.sendError(channel, "That's too long. Please try something shorter.");
            return;
        }
        ShortcutData.getInstanceByGuildId(guild.getId()).setEntry(newTrigger, newOutput);
        MessagingUtils.sendCompleted(channel, "Added!");
        System.out.println("Added " + newTrigger + " with output " + newOutput);
    }
}
