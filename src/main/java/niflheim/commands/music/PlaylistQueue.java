package niflheim.commands.music;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        aliases = {"pq"},
        help = "Queues a playlist from a URL.",
        usage = ".pq <Url>",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.GUILD
)
public class PlaylistQueue extends Command {
    public void execute(Context context, String[] args) {

    }
}