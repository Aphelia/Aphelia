package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import to.us.awesomest.aphelia.data.ShortcutData;

public class DelShortcut implements Command {

    @Override
    public String getName() {
        return "delShortcut";
    }

    @Override
    public String getDescription() {
        return "Delete a shortcut.";
    }

    @Override
    public boolean isDMUsable() {
        return false;
    }

    @Override
    public void run(Message message) {
        User author = message.getAuthor();
        MessageChannel channel = message.getChannel();
        String args = CommandUtils.getArgs(message.getContentRaw());
        Guild guild = message.getGuild();
        Member commander = guild.getMember(author);
        if(args == null) {
            channel.sendMessage("Usage: !delShortcut <command case-insensitive>").queue();
            return;
        }
        String[] commandArgsArray = args.split(" ");
        assert commander != null;
        if(!commander.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("Not enough permissions! You must have **Manage Server**").queue();
            return;
        }

        if(commandArgsArray.length != 1) {
            channel.sendMessage("Usage: !delShortcut <command case-insensitive>").queue();
        }

        if(!ShortcutData.getInstanceByGuildId(guild.getId()).hasEntry(commandArgsArray[0])){
            channel.sendMessage("Error: That shortcut does not exist!").queue();
            return;
        }
        ShortcutData.getInstanceByGuildId(guild.getId()).deleteEntry(commandArgsArray[0]);
        channel.sendMessage("Deleted!").queue();
        System.out.println("Deleted " + commandArgsArray[0]);
    }
}
