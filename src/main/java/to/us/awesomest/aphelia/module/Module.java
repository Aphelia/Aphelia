package to.us.awesomest.aphelia.module;

import net.dv8tion.jda.api.entities.Message;

public interface Module {
    boolean run(Message message);
    String getName();
    String getDescription();
}
