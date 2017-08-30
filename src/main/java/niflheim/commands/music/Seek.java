package niflheim.commands.music;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        help = "Skips to a time in the song.",
        usage = ".skip hh:mm:ss",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.VOICE
)
public class Seek extends Command {
    public void execute(Context context, String[] args) {

    }
}