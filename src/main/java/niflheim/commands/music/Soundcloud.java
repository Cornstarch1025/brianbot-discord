package niflheim.commands.music;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        aliases = {"sc"},
        help = "Searches and plays a song from SoundCloud given search words.",
        usage = ".sc <Search>",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.GUILD
)
public class Soundcloud extends Command {
    public void execute(Context context, String[] args) {

    }
}