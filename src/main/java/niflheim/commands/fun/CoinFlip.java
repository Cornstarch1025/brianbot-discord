package niflheim.commands.fun;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        name = "CoinFlip",
        example = ".coinflip",
        aliases = {"flip"},
        help = "Flips a coin!",
        usage = ".coinflip",
        cooldown = 3000L,
        category = Category.FUN,
        scope = Scope.GUILD
)
public class CoinFlip extends Command {
    private final String[] sides = {"`Heads`", "`Tails`", "`The Middle???`"};

    public void execute(Context context, String[] args) {
        if (args.length > 0) {
            context.invalid(this);
            return;
        }

        int random = (int) (Math.random() * 100);
        int random2 = (int) (Math.random() * 100);

        if (random == random2)
            context.channel.sendMessage("You flipped " + sides[2]).queue();
        else
            context.channel.sendMessage("You flipped " + sides[random % 2]).queue();
    }
}