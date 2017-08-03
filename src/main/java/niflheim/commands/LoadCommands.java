package niflheim.commands;

import niflheim.commands.admin.*;
import niflheim.commands.general.Ping;
import niflheim.commands.mod.*;
import niflheim.core.Core;

public class LoadCommands {
    public static void init() {
        //Admin commands
        Core.registerCommand("eval", new Eval());
        Core.registerCommand("msg", new Msg());
        Core.registerCommand("revive", new Revive());
        Core.registerCommand("search", new Search());
        Core.registerCommand("setavatar", new SetAvatar());
        Core.registerCommand("setname", new SetName());
        Core.registerCommand("shutdown", new SetAvatar());
        Core.registerCommand("toggle", new Toggle());

        //Fun commands

        //General commands
        Core.registerCommand("ping", new Ping());

        //Info commands

        //Mod commands
        Core.registerCommand("autorole", new AutoRole());
        Core.registerCommand("ban", new Ban());
        Core.registerCommand("goodbye", new Goodbye());
        Core.registerCommand("kick", new Kick());
        Core.registerCommand("prune", new Prune());
        Core.registerCommand("setchannel", new SetChannel());
        Core.registerCommand("setprefix", new SetPrefix());
        Core.registerCommand("tempban", new TempBan());
        Core.registerCommand("welcome", new Welcome());

        //Music commands

        //Utility commands
    }
}
