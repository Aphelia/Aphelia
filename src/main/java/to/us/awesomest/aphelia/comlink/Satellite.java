package to.us.awesomest.aphelia.comlink;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.google.gson.Gson;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.slf4j.LoggerFactory;
import to.us.awesomest.aphelia.Aphelia;
import to.us.awesomest.aphelia.data.MCData;
import to.us.awesomest.aphelia.data.WebhookData;
import to.us.awesomest.aphelia.module.MessagingUtils;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.regex.Pattern;

@SuppressWarnings({"InfiniteLoopStatement", "SpellCheckingInspection"})

public class Satellite extends Thread {
    static SecureRandom random = new SecureRandom();
    byte[] seed;

    private static final HashMap<String, DataOutputStream> outputSocketMap = new HashMap<>();
    static Gson json = new Gson();
    private static Satellite instance;
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + '§' + "[0-9A-FK-OR]");

    private Satellite() {
    }

    public static Satellite getInstance() {
        if (instance == null) {
            instance = new Satellite();
            instance.seed = random.generateSeed(4);
        }
        return instance;
    }

    private static String generateToken(@SuppressWarnings("SameParameterValue") int length) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz"
                + "!#$%()*+,-./";

        // create StringBuffer size of AlphaNumericString
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = (int) (AlphaNumericString.length() * random.nextDouble());
            // add Character one by one in end of stringBuilder
            stringBuilder.append(AlphaNumericString.charAt(index));
        }
        return stringBuilder.toString();
    }

    public void run() {
        System.out.println("Started socket!");
        try {
            ServerSocket serverSocket;
            if (Aphelia.isBeta()) {
                serverSocket = new ServerSocket(2564);
            } else {
                serverSocket = new ServerSocket(2565);
            }
            while (true) {
                try {
                    new ClientHandler(serverSocket.accept()).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasOutputStream(String channelId) {
        return outputSocketMap.containsKey(channelId);
    }

    public static void passChatMessage(String channelId, String username, String message) {
        if (!outputSocketMap.containsKey(channelId))
            throw new NullPointerException("No socket for that channel found!");
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("type", "CHAT");
        dataMap.put("content", stripColor(message));
        dataMap.put("user", stripColor(username));
        try {
            outputSocketMap.get(channelId).writeUTF(json.toJson(dataMap));
        } catch (IOException ignored) {
            outputSocketMap.remove(channelId);
            LoggerFactory.getLogger("Satellite").debug("Caught IOException in method passChatMessage, assuming disconnected server, removing from cache.");
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        private static void processByType(TextChannel discordChannel, HashMap<String, String> data) {
            switch(data.get("type")) {
                case "CHAT":
                    /*discordChannel.sendMessage("**" + data.get("user") + "**: " + stripColor(data.get("content"))).queue();
                    System.out.println("Sent " + stripColor(data.get("content")));
                    System.out.println("Listening...");*/
                    try {
                        if (!WebhookData.getInstanceByGuildId(discordChannel.getGuild().getId()).hasEntry(discordChannel.getId()))
                            WebhookData.getInstanceByGuildId(discordChannel.getGuild().getId()).setEntry(discordChannel.getId(), discordChannel.createWebhook("Aphelia Webhook").complete().getUrl());
                    } catch(InsufficientPermissionException e) {
                        MessagingUtils.sendError(discordChannel, "I need the permission **Manage Webhooks**");
                        return;
                    }
                    WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder().setContent(data.get("content")).setUsername(data.get("user") + " (on Minecraft)").setAvatarUrl("https://minecraftskinstealer.com/api/v1/skin/download/bust/" + data.get("user"));
                    WebhookClient.withUrl(WebhookData.getInstanceByGuildId(discordChannel.getGuild().getId()).getEntry(discordChannel.getId())).send(messageBuilder.build());
                    break;
                case "JOIN":
                    EmbedBuilder joinMessageBuilder = new EmbedBuilder();
                    joinMessageBuilder
                            .setColor(new Color(0, 255, 0))
                            .setTitle("Join")
                            .setDescription(data.get("user") + " joined the server! (" + data.get("online") + "/" + data.get("max") + " playing)");
                    discordChannel.sendMessage(joinMessageBuilder.build()).queue();
                    break;
                case "LEAVE":
                    EmbedBuilder leaveMessageBuilder = new EmbedBuilder();
                    leaveMessageBuilder
                            .setColor(new Color(255, 0, 0))
                            .setTitle("Leave")
                            .setDescription(data.get("user") + " left the server. (" + data.get("online") + "/" + data.get("max") + " playing)");
                    discordChannel.sendMessage(leaveMessageBuilder.build()).queue();
                    break;
                case "DEATH":
                    EmbedBuilder deathMessageBuilder = new EmbedBuilder();
                    deathMessageBuilder
                            .setColor(new Color(63, 0, 0))
                            .setTitle("Death")
                            .setDescription(data.get("user") + " died.");
                    discordChannel.sendMessage(deathMessageBuilder.build()).queue();
                    break;
                case "DISCONNECT":
                    EmbedBuilder disconnectMessageBuilder = new EmbedBuilder();
                    disconnectMessageBuilder
                            .setColor(new Color(0, 0, 0))
                            .setTitle("Server Death")
                            .setDescription("The Minecraft server has died.");
                    discordChannel.sendMessage(disconnectMessageBuilder.build()).queue();
                    discordChannel.getManager().setTopic("Server dead | Hermes by Aphelia").queue();
                    break;
                case "CONNECT":
                    EmbedBuilder connectMessageBuilder = new EmbedBuilder();
                    connectMessageBuilder
                            .setColor(new Color(255, 255, 255))
                            .setTitle("Server Connected")
                            .setDescription("The Minecraft server has connected to Aphelia.");
                    discordChannel.sendMessage(connectMessageBuilder.build()).queue();
                    discordChannel.getManager().setTopic("Server online | Hermes by Aphelia").queue();
                    break;
            }
        }

        @SuppressWarnings({"ConstantConditions", "unchecked"})
        public void run() {
            try {
                while (true) {
                    DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
                    String str = inputStream.readUTF();
                    LoggerFactory.getLogger("Satellite").debug(str);
                    HashMap<String, String> data = json.fromJson(str, HashMap.class);

                    if(!data.containsKey("protocol") || !data.get("protocol").equals("H1")) {
                        outputStream.writeUTF("{\"type\":\"CHAT\", \"content\": \"§bThis §bversion §bof §bthe §bHermes §bfor §bAphelia §bis §bincompatible §bwith §bthis §bversion §bof §bAphelia §bHermes §bServer. §bPlease §bupdate §byour §bplugin.§r\", \"user\": \"§cAphelia §cSystem§r\"}");
                        clientSocket.close();
                        LoggerFactory.getLogger("Satellite").debug("Rejected client that had an unsupported protocol version.");
                    }

                    if (data.get("type").equals("TOKENREQUEST")) {
                        HashMap<String, String> responseMap = new HashMap<>();
                        responseMap.put("response", generateToken(64));
                        outputStream.writeUTF(json.toJson(responseMap));
                        LoggerFactory.getLogger("Tokens").info("Granted request for a token!");
                        continue;
                    }

                    LoggerFactory.getLogger("Satellite").debug("Channel:" + MCData.getInstance().getEntry(data.get("token")));
                    if (!MCData.getInstance().hasEntry(data.get("token"))) {
                        LoggerFactory.getLogger("Satellite").debug("There wasn't a token in the message; rejected.");
                        continue;
                    }

                    if (data.get("type").equals("IDENTIFY")) {
                        TextChannel associatedChannel = (TextChannel) Aphelia.bot.getGuildChannelById(MCData.getInstance().getEntry(data.get("token")));
                        outputSocketMap.put(associatedChannel.getId(), outputStream);
                        LoggerFactory.getLogger("Satellite").debug("Received heartbeat message");
                        continue;
                    }

                    TextChannel associatedChannel = (TextChannel) Aphelia.bot.getGuildChannelById(MCData.getInstance().getEntry(data.get("token")));
                    outputSocketMap.put(associatedChannel.getId(), outputStream);
                    if (associatedChannel == null) {
                        System.out.println("Received message with invalid token!");
                        continue;
                    }
                    processByType(associatedChannel, data);
                }
            } catch (IOException ignored) {
                //probably just some client disconnecting
            }
        }
    }
    public static String stripColor(String input) {
        return input == null ? null : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
