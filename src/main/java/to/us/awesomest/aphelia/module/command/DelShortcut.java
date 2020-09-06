package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import to.us.awesomest.aphelia.data.ShortcutData;
import to.us.awesomest.aphelia.globalutils.LanguageUtils;
import to.us.awesomest.aphelia.module.MessagingUtils;

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
    public boolean run(Message message) {
        Member commander = message.getGuild().getMember(message.getAuthor());

        String[] commandArgsArray = CommandUtils.getArgs(message.getContentRaw()).split(" ");
        assert commander != null;
        if(!commander.hasPermission(Permission.MANAGE_SERVER)) {
            MessagingUtils.sendNoPermissions(message.getChannel(), LanguageUtils.getMessage(message.getGuild(), "permissionManageServer"));
            return false;
        }

        if(commandArgsArray.length != 1) {
            message.getChannel().sendMessage("Usage: !delShortcut <command case-insensitive>").queue();
        }

        if(!ShortcutData.getInstanceByGuildId(message.getGuild().getId()).hasEntry(commandArgsArray[0])){
            MessagingUtils.sendNoPermissions(message.getChannel(), LanguageUtils.getMessage(message.getGuild(), "permissionManageServer"));
            return false;
        }
        ShortcutData.getInstanceByGuildId(message.getGuild().getId()).deleteEntry(commandArgsArray[0]);
        message.getChannel().sendMessage(LanguageUtils.getMessage(message.getGuild(), "successShortcutDeleted")).queue();
        System.out.println("Deleted " + commandArgsArray[0]);
        return true;
    }
}
