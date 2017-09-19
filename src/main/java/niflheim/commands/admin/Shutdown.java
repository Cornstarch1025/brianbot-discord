package niflheim.commands.admin;

import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.core.Context;
import niflheim.core.Shard;

@CommandFrame(
        name = "Shutdown",
        example = ".shutdown",
        help = "Shuts down the bot.",
        usage = ".shutdown",
        admin = true,
        toggleable = false,
        category = Category.ADMIN
)
public class Shutdown extends Command {
    @Override
    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        context.channel.sendMessage("As you wish.").queue(s -> {
            Okita.monitor.shutdown();

            for (Shard shard : Okita.shards)
                shard.getJda().shutdown();
        });
    }
}
