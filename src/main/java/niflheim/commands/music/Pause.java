package niflheim.commands.music;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        aliases = {"p"},
        help = "Pauses the currently playing track.",
        usage = ".pause",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.GUILD
)
public class Pause extends Command {
    public void execute(Context context, String[] args) {

    }
}