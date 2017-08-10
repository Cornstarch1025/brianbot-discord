package niflheim.commands.music;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        aliases = {"yt"},
        help = ".yt <Search>",
        usage = "Searches and plays a song from Youtube given a search.",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.GUILD
)
public class Youtube extends Command {
    public void execute(Context context, String[] args) {

    }
}