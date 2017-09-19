package niflheim.commands.music;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        name = "RemoveQueue",
        example = ".removequeue 1",
        aliases = {"rq"},
        help = "Removes an item from the queue.",
        usage = ".rq <Number>",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.VOICE
)
public class RemoveQueue extends Command {
    public void execute(Context context, String[] args) {

    }
}