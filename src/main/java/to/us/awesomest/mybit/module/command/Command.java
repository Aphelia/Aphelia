package to.us.awesomest.mybit.module.command;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import to.us.awesomest.mybit.module.Module;

import javax.annotation.Nullable;

public interface Command extends Module {
    boolean isDMUsable();
}
