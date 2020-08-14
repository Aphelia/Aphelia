package to.us.awesomest.aphelia.module;

import net.dv8tion.jda.api.entities.Message;
import org.slf4j.LoggerFactory;
import to.us.awesomest.aphelia.data.PrefixData;
import to.us.awesomest.aphelia.module.command.*;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    private static final Map<String, ModuleManager> managerMap = new HashMap<>();
    private String prefix = "!";
    private static ModuleManager DMInstance;
    private String guildId;

    private final Command[] enabledCommands = {
            new CheckComs(),
            new NewShortcut(),
            new DelShortcut(),
            new Help(),
            new Pay(),
            new ConnectMC(),
            new Balance(),
            new GiveCoins(),
            new TakeCoins(),
            new Ticket(),
            new CloseTicket(),
            new Suggest(),
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

    public boolean runCommands(Message message) {
        String commandName = message.getContentRaw();
        if(message.getContentRaw().contains(" "))
            commandName = message.getContentRaw().substring(0, message.getContentRaw().indexOf(' '));
        for (Command commandClass : enabledCommands) {
            if (!commandName.equalsIgnoreCase(prefix + commandClass.getName())) {
                continue;
            }
            if (!commandClass.isDMUsable() && !message.isFromGuild()) {
                message.getChannel().sendMessage("Error encountered: This command cannot be used in a DM.").queue();
                LoggerFactory.getLogger("ModuleManager").debug("Rejected request to use " + message.getContentRaw() + " while not in a guild!");
                return false;
            }
            LoggerFactory.getLogger("ModuleManager").debug("Sent " + message.getContentRaw() + " to " + commandClass.getName());
            commandClass.run(message);
            return true;
        }
        return false;
    }
}
