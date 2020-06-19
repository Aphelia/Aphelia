package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import to.us.awesomest.aphelia.module.MessagingUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Inform implements Command {
    @Override
    public boolean isDMUsable() {
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void run(User author, MessageChannel channel, String args, Guild guild) {
        List<String> argsArray = new ArrayList<>(Arrays.asList(args.split(" ")));
        if (argsArray.size() < 2) {
            channel.sendMessage("Invalid syntax! Usage: !inform <title> <description> <line 1> <line 2> ... <line *n*> (Replace spaces with underscores).").queue();
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
        informBuilder.setTitle(argsArray.remove(0).replace("_", " "));
        informBuilder.setDescription(argsArray.remove(0).replace("_", " "));
        for (String arg : argsArray) {
            informBuilder.addField(" ", arg.replace("_", " "), false);
        }
        informBuilder.addField(" ", "*Like this? [Invite me](https://aphelia.github.io/invite)!*", false);
        informBuilder.setFooter("Sent by " + author.getName(), author.getAvatarUrl());
        channel.sendMessage(informBuilder.build()).queue();
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
