package to.us.awesomest.mybit;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import to.us.awesomest.mybit.comlink.Satellite;
import to.us.awesomest.mybit.listeners.ChatListener;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class MyBit {
    public static JDA bot;
    final static private String version = "Release 0.10";

    public static String getVersion() {
        return version;
    }
    private static String getToken() throws FileNotFoundException {
        String token;
        File file = new File("token");
        Scanner tokenScanner = new Scanner(file);
        token = tokenScanner.nextLine();
        tokenScanner.close();
        return token;
    }
    public static void main(String[] args) throws LoginException {
        String token = null;
        try {
            token = getToken();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to find token. Aborting.");
            System.exit(1);
        }
        Collection<GatewayIntent> intents = new ArrayList<>();
        intents.add(GatewayIntent.GUILD_MESSAGES);
        intents.add(GatewayIntent.DIRECT_MESSAGES);
        bot = JDABuilder.create(token, intents)
                .setEventManager(new AnnotatedEventManager())
                .addEventListeners(new ChatListener())
                .build();
        bot.getPresence().setActivity(Activity.playing("!help | " + getVersion()));
        System.out.println("Started Bot!");
        Satellite.getInstance().start();
    }
}
