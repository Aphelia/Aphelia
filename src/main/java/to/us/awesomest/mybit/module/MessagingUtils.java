package to.us.awesomest.mybit.module;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;

public final class MessagingUtils {
    public static void sendError(MessageChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setTitle("Error")
                .setColor(new Color(255, 0, 0))
                .addField(" ", "An error occurred. Did you specify the right arguments?", false);
        channel.sendMessage(embedBuilder.build()).queue();
    }

    public static void sendNoPermissions(MessageChannel channel, String permission) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setColor(new Color(255, 0, 0))
                .setTitle("Insufficient Permissions!")
                .setDescription("You must have **" + permission + "**.");
        channel.sendMessage(embedBuilder.build()).queue();
    }
    public static void sendCompleted(MessageChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setColor(new Color(0, 255, 0))
                .setTitle("Task Completed!")
                .setDescription("Thank you for using MyBit.");
        channel.sendMessage(embedBuilder.build()).queue();
    }
}
