package to.us.awesomest.mybit.module;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.LoggerFactory;
import to.us.awesomest.mybit.module.command.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    private static final Map<String, ModuleManager> managerMap = new HashMap<>();
    private String prefix = "!";
    private static ModuleManager DMInstance;
    private Command[] enabledCommands = {
            new CheckComs(),
            new NewShortcut(),
            new DelShortcut(),
            new Help(),
            new ConnectMC(),
            new Balance(),
            new GiveCoins(),
            new TakeCoins(),
            new Gamble(),
            new ChangePrefix()};

    public static ModuleManager getInstanceByGuildId(String guildId) {
        if(!managerMap.containsKey(guildId))
            managerMap.put(guildId, new ModuleManager());
        return managerMap.get(guildId);
    }
    public static ModuleManager getDMInstance() {
        if(DMInstance == null) {
            DMInstance = new ModuleManager();
        }
        return DMInstance;
    }

    public Command[] getEnabledCommands() {
        return enabledCommands;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean runCommands(User user, MessageChannel channel, String command, @Nullable Guild guild) {
        String commandName;
        LoggerFactory.getLogger("ModuleManager").info("Received text " + command);
        if(command.contains(" ")) {commandName = command.split(" ")[0];}
        else {commandName = command;}
        for (Command commandClass:enabledCommands) {
            if(!commandName.equalsIgnoreCase(prefix + commandClass.getName())) {
                LoggerFactory.getLogger("ModuleManager").debug(prefix + commandClass.getName() + " is not equal to " + commandName + ", skipping.");
                continue;
            }
            if(!commandClass.isDMUsable() && guild == null) {
                channel.sendMessage("Error encountered: This command cannot be used in a DM.").queue();
                LoggerFactory.getLogger("ModuleManager").debug("Rejected request to use " + command + " while not in a guild!");
                return false;
            }
            LoggerFactory.getLogger("ModuleManager").debug("Sent " + command + " to " + commandClass.getName());
            String args;
            try {
               args = command.substring(command.indexOf(" ") + 1);
            }
            catch(StringIndexOutOfBoundsException e) {
                args = null;
            }
            commandClass.run(user, channel, args, guild);
            return true;
        }
        return false;
    }
}
