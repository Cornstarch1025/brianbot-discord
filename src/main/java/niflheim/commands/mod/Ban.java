package niflheim.commands.mod;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        aliases = {},
        help = "",
        usage = "",
        cooldown = 3000L,
        level = 0,
        guildOwner = false,
        admin = false,
        toggleable = true,
        category = Category.GENERAL,
        scope = Scope.GUILD,
        permissions = {}
)
public class Ban extends Command {
    public void execute(Context context, String[] args) {

    }
}