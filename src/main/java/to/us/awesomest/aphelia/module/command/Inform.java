package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import to.us.awesomest.aphelia.module.MessagingUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Inform implements Command {
    @Override
    public boolean isDMUsable() {
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void run(User author, MessageChannel channel, String args, Guild guild) {
        List<String> argsArray = Arrays.asList(args.split(" "));
        if (argsArray.size() < 2) {
            channel.sendMessage("Invalid syntax! Usage: !inform <title> <description> <line 1> <line 2> ... <line *n*>");
            return;
        }
        if (guild != null) {
            if (!guild.getMember(author).hasPermission(Permission.MANAGE_SERVER)) {
                MessagingUtils.sendNoPermissions(channel, "Manage Server");
                return;
            }
        }
        EmbedBuilder informBuilder = new EmbedBuilder();
        informBuilder.setColor(new Color(0, 22, 88));
        informBuilder.setTitle(argsArray.remove(1));
        informBuilder.setDescription(argsArray.remove(1));
        for (String arg : argsArray) {
            informBuilder.addField(" ", arg, false);
        }
        informBuilder.addBlankField(false);
        informBuilder.addField(" ", "*Like this? [Invite me!](aphelia.github.io/invite)*", false);
        informBuilder.setFooter("Sent by " + author.getName(), author.getAvatarUrl());
    }

    @Override
    public String getName() {
        return "inform";
    }

    @Override
    public String getDescription() {
        return "Inform users in an embedded way.";
    }
}
