package niflheim.commands.music;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        help = "Restarts the current song.",
        usage = ".restart",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.VOICE
)
public class Restart extends Command {
    public void execute(Context context, String[] args) {

    }
}