package niflheim.commands.music;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        aliases = "np",
        help = "Shows currently playing song as well as position.",
        usage = ".np",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.GUILD
)
public class NowPlaying extends Command {
    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }


    }
}