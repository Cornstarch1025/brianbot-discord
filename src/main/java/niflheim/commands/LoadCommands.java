package niflheim.commands;

import niflheim.commands.admin.*;
import niflheim.commands.fun.*;
import niflheim.commands.general.*;
import niflheim.commands.info.Avatar;
import niflheim.commands.info.Permissions;
import niflheim.commands.info.Server;
import niflheim.commands.info.User;
import niflheim.commands.mod.*;
import niflheim.commands.music.Join;
import niflheim.commands.music.Leave;
import niflheim.commands.utility.Spoiler;
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
        Core.registerCommand("choose", new Choose());
        Core.registerCommand("coinflip", new CoinFlip());
        Core.registerCommand("eightball", new EightBall());
        Core.registerCommand("mock", new Mock());
        Core.registerCommand("reverse", new Reverse());
        Core.registerCommand("roulette", new Roulette());

        //General commands
        Core.registerCommand("bot", new Bot());
        Core.registerCommand("donate", new Donate());
        Core.registerCommand("help", new Help());
        Core.registerCommand("invite", new Invite());
        Core.registerCommand("ping", new Ping());
        Core.registerCommand("support", new Support());
        Core.registerCommand("uptime", new Uptime());

        //Info commands
        Core.registerCommand("avatar", new Avatar());
        Core.registerCommand("permissions", new Permissions());
        Core.registerCommand("server", new Server());
        Core.registerCommand("user", new User());

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
        Core.registerCommand("join", new Join());
        Core.registerCommand("leave", new Leave());

        //Utility commands
        Core.registerCommand("spoiler", new Spoiler());
    }
}
