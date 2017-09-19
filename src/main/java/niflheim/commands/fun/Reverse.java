package niflheim.commands.fun;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        name = "Reverse",
        example = ".reverse Hello World!",
        help = "Reverses any given text.",
        usage = ".reverse <Text>",
        cooldown = 3000L,
        category = Category.FUN,
        scope = Scope.GUILD
)
public class Reverse extends Command {
    public void execute(Context context, String[] args) {
        StringBuilder text = new StringBuilder();

        switch (args.length) {
            default:
                for (String x : args)
                    text.append(x).append(" ");

                text.deleteCharAt(text.length() - 1);

                context.channel.sendMessage(text.reverse().toString()).queue();
                break;
            case 0:
                context.invalid(this);
                break;
        }
    }
}