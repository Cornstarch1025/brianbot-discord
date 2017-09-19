package niflheim.commands.admin;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.core.Context;

@CommandFrame(
        name = "SetName",
        example = ".setname Okita",
        help = "Sets a new name for the bot.",
        usage = ".setname <Name>",
        admin = true,
        toggleable = false,
        category = Category.ADMIN
)
public class SetName extends Command {
    @Override
    public void execute(Context context, String[] args) {
        if (args.length == 0) {
            context.invalid(this);
            return;
        }

        StringBuilder name = new StringBuilder();

        for (String str : args)
            name.append(str).append(" ");

        context.jda.getSelfUser().getManager().setName(name.toString()).queue(success -> context.channel.sendMessage("Successfully changed bot name.").queue());
    }
}
