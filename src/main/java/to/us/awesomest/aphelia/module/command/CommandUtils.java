package to.us.awesomest.aphelia.module.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.slf4j.LoggerFactory;

import java.util.List;

final class CommandUtils {
    static Member parseUser(Guild guild, String string) {
        if(string.matches("^<@!(\\d+)>$")) {
            String userId = string.substring(3, string.length() - 1);
            LoggerFactory.getLogger("CommandUtils").debug("Got userID " + userId);
            Member member = guild.getMemberById(userId);
            if(member == null) throw new IllegalArgumentException("There is no such user!");
            return member;
        } if(string.matches("^<@(\\d+)>$")) {
            String userId = string.substring(2, string.length() - 1);
            LoggerFactory.getLogger("CommandUtils").debug("Got userID " + userId);
            Member member = guild.getMemberById(userId);
            if(member == null) throw new IllegalArgumentException("There is no such user!");
            return member;
        } if(string.matches("^\\d+$")) {
            LoggerFactory.getLogger("CommandUtils").debug("Got userID " + string);
            Member member = guild.getMemberById(string);
            if(member == null) throw new IllegalArgumentException("There is no such user!");
            return member;
        } if(string.matches("^.*#\\d{4}$")) {
            LoggerFactory.getLogger("CommandUtils").debug("Got usertag " + string);
            Member member = guild.getMemberByTag(string);
            if(member == null) throw new IllegalArgumentException("There is no such user!");
            return member;
        } else {
            LoggerFactory.getLogger("CommandUtils").debug("Got username " + string);
            List<Member> memberList = guild.getMembersByEffectiveName(string, true);
            if(memberList.size() == 0) throw new IllegalArgumentException("There is no such user!");
            Member member = memberList.get(0);
            if(member == null) throw new IllegalArgumentException("There is no such user!");
            return member;
        }
    }
}