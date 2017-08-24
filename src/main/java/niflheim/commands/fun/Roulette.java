package niflheim.commands.fun;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        help = "A good ol game of Russian Roulette!",
        usage = ".roulette",
        cooldown = 3000L,
        category = Category.FUN,
        scope = Scope.GUILD
)
public class Roulette extends Command {
    private final String[] outcomes = {"Phew! You live to see another day.", "What luck! You survive.", "You pushed your luck, but at least you survived.", "Boom! Your head limps to the side while blood splatters everywhere.", "*Tch* There was no bullet in that chamber!", "The cylinder rotates harmlessly."};

    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        context.channel.sendMessage(outcomes[(int) (Math.random() * 6)]).queue();
    }
}