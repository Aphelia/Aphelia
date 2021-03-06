package to.us.awesomest.aphelia.comlink;

import com.google.gson.Gson;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.LoggerFactory;
import to.us.awesomest.aphelia.Aphelia;
import to.us.awesomest.aphelia.data.MCData;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.HashMap;

@SuppressWarnings({"InfiniteLoopStatement", "SpellCheckingInspection"})

public class Satellite extends Thread {
    static SecureRandom random = new SecureRandom();
    byte[] seed;

    private static final HashMap<String, DataOutputStream> outputSocketMap = new HashMap<>();
    static Gson json = new Gson();
    private static Satellite instance;

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
        dataMap.put("content", message);
        dataMap.put("user", username);
        try {
            outputSocketMap.get(channelId).writeUTF(json.toJson(dataMap));
        } catch (IOException e) {
            e.printStackTrace();
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
                    discordChannel.sendMessage("**" + data.get("user") + "**: " + data.get("content")).queue();
                    System.out.println("Sent " + data.get("content"));
                    System.out.println("Listening...");
                    break;
                case "JOIN":
                    EmbedBuilder joinMessageBuilder = new EmbedBuilder();
                    joinMessageBuilder
                            .setColor(new Color(0, 255, 0))
                            .setTitle("Join")
                            .setDescription(data.get("user") + " joined the server!");
                    discordChannel.sendMessage(joinMessageBuilder.build()).queue();
                    break;
                case "LEAVE":
                    EmbedBuilder leaveMessageBuilder = new EmbedBuilder();
                    leaveMessageBuilder
                            .setColor(new Color(255, 0, 0))
                            .setTitle("Leave")
                            .setDescription(data.get("user") + " left the server.");
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
                    if (data.get("type").equals("TOKENREQUEST")) {
                        HashMap<String, String> responseMap = new HashMap<>();
                        responseMap.put("response", generateToken(64));
                        outputStream.writeUTF(json.toJson(responseMap));
                        LoggerFactory.getLogger("Tokens").info("Granted request for a token!");
                        continue;
                    }

                    LoggerFactory.getLogger("Satellite").debug("Channel:" + MCData.getInstance().getEntry(data.get("token")));
                    if (!MCData.getInstance().hasEntry(data.get("token"))) {
                        LoggerFactory.getLogger("Satellite").debug("Found invalid token, rejecting.");
                        continue;
                    }

                    if (data.get("type").equals("HEARTBEAT")) {
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
}
