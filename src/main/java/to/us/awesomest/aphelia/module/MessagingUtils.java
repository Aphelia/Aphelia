package to.us.awesomest.aphelia.module;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;

public final class MessagingUtils {
    public static void sendError(MessageChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setTitle("Error")
                .setColor(new Color(255, 0, 0))
                .setDescription("An error occurred. Did you specify the right arguments?");
        channel.sendMessage(embedBuilder.build()).queue();
    }

    public static void sendError(MessageChannel channel, String error) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setTitle("Error")
                .setColor(new Color(255, 0, 0))
                .setDescription(error);
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
                .setDescription("Thank you for using Aphelia.");
        channel.sendMessage(embedBuilder.build()).queue();
    }

    public static void sendCompleted(MessageChannel channel, String text) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setColor(new Color(0, 255, 0))
                .setTitle("Task Completed!")
                .setDescription(text);
        channel.sendMessage(embedBuilder.build()).queue();
    }

    public static void sendInfo(MessageChannel channel, String text) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setColor(new Color(0, 22, 88))
                .setDescription(text);
        channel.sendMessage(embedBuilder.build()).queue();
    }
}
