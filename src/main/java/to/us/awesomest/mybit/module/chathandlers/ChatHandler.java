package to.us.awesomest.mybit.module.chathandlers;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import to.us.awesomest.mybit.module.Module;


public interface ChatHandler extends Module {
    boolean adminException();
}
