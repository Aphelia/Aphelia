package to.us.awesomest.aphelia.module;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.LoggerFactory;
import to.us.awesomest.aphelia.data.PrefixData;
import to.us.awesomest.aphelia.module.command.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    private static final Map<String, ModuleManager> managerMap = new HashMap<>();
    private String prefix = "!";
    private static ModuleManager DMInstance;
    private String guildId;

    @SuppressWarnings("FieldMayBeFinal") //adding new feature in future
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
            new ChangePrefix(),
            new Inform()};

    public static ModuleManager getInstanceByGuildId(String guildId) {
        if (!managerMap.containsKey(guildId)) {
            managerMap.put(guildId, new ModuleManager());
            managerMap.get(guildId).guildId = guildId;
            String possiblePrefix = PrefixData.getInstanceByGuildId(guildId).getEntry("prefix");
            if (possiblePrefix != null) managerMap.get(guildId).prefix = possiblePrefix;
        }
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
        PrefixData.getInstanceByGuildId(guildId).setEntry("prefix", prefix);
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
