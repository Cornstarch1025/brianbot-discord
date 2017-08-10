package niflheim.commands.music;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        help = "Skips the currently playing song.",
        usage = ".skip",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.GUILD
)
public class Skip extends Command {
    public void execute(Context context, String[] args) {

    }
}