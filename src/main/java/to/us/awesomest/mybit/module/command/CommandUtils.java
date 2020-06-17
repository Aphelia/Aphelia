package to.us.awesomest.mybit.module.command;

final class CommandUtils {
    static String[] parseArgs(String argString) {
        return argString.split(" ");
    }
}
