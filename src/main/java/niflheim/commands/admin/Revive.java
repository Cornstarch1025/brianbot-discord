package niflheim.commands.admin;

import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.core.Context;
import niflheim.utils.Settings;

@CommandFrame(
        aliases = "restart",
        help = "Revives a targeted shard manually.",
        usage = ".revive <Shard ID>",
        admin = true,
        toggleable = false,
        category = Category.ADMIN
)
public class Revive extends Command {
    @Override
    public void execute(Context context, String[] args) {
        if (args.length != 1) {
            context.invalid(this);
            return;
        }

        try {
            Long input = Long.valueOf(args[0]);

            if (input < Settings.SHARDS && input > -1)
                Okita.shards.get(Integer.parseInt(args[0])).revive();
            else if (Okita.getGuildByID(args[0]) != null)
                Okita.shards.get(Okita.getGuildByID(args[0]).getJDA().getShardInfo().getShardId()).revive();
            else
                context.channel.sendMessage("Shard not found!").queue();
        } catch (Exception e) {
            context.invalid(this);
        }
    }
}
