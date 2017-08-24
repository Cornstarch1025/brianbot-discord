package niflheim.commands.fun;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.util.Arrays;

@CommandFrame(
        aliases = {"pick"},
        help = "Picks an item given a semicolon separated list.",
        usage = ".choose item;item;item...",
        cooldown = 3000L,
        category = Category.FUN,
        scope = Scope.GUILD
)
public class Choose extends Command {
    public void execute(Context context, String[] args) {
        if (args.length == 0 || !Arrays.toString(args).contains(";")) {
            context.invalid(this);
            return;
        }

        StringBuilder list = new StringBuilder();

        for (String str : args)
            list.append(str).append(" ");

        String[] items = list.toString().split(";");

        context.channel.sendMessage("Hm... I chose `" + items[(int) (Math.random() * (items.length + 1))] + "`.").queue();
    }
}