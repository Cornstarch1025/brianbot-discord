package niflheim.commands;

import niflheim.commands.general.Ping;
import niflheim.core.Core;

public class LoadCommands {
    public static void init() {
        Core.registerCommand("ping", new Ping());
    }
}
