package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
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
        Member commander = message.getGuild().getMember(message.getAuthor());

        String[] commandArgsArray = CommandUtils.getArgs(message.getContentRaw()).split(" ");
        assert commander != null;
        if(!commander.hasPermission(Permission.MANAGE_SERVER)) {
            message.getChannel().sendMessage("Not enough permissions! You must have **Manage Server**").queue();
            return;
        }

        if(commandArgsArray.length != 1) {
            message.getChannel().sendMessage("Usage: !delShortcut <command case-insensitive>").queue();
        }

        if(!ShortcutData.getInstanceByGuildId(message.getGuild().getId()).hasEntry(commandArgsArray[0])){
            message.getChannel().sendMessage("Error: That shortcut does not exist!").queue();
            return;
        }
        ShortcutData.getInstanceByGuildId(message.getGuild().getId()).deleteEntry(commandArgsArray[0]);
        message.getChannel().sendMessage("Deleted!").queue();
        System.out.println("Deleted " + commandArgsArray[0]);
    }
}
