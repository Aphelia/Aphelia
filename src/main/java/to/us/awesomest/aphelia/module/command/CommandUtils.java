package to.us.awesomest.aphelia.module.command;

final class CommandUtils {
    static String[] parseArgs(String argString) {
        return argString.split(" ");
    }
}
