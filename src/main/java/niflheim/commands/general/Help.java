package niflheim.commands.general;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        aliases = {"halp"},
        help = "Displays help information on modules as well as commands.",
        usage = ".help, .help <Module>, .help <Command>",
        cooldown = 3000L,
        category = Category.GENERAL,
        scope = Scope.GUILD
)
public class Help extends Command {
    public void execute(Context context, String[] args) {

    }
}