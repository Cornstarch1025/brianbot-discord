package niflheim.commands.mod;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;
import niflheim.rethink.GuildOptions;

@CommandFrame(
        help = "Set's a designated channel for guild welcome and goodbye messages. If no channel is set welcome and goodbye messages will send in the public channel.",
        usage = ".setchannel, .setchannel clear",
        cooldown = 3000L,
        guildOwner = true,
        category = Category.MOD,
        scope = Scope.GUILD
)
public class SetChannel extends Command {
    public void execute(Context context, String[] args) {
        GuildOptions options = context.guildOptions;

        switch (args.length) {
            default:
                context.invalid(this);
                break;
            case 0:
                options.setMessageChannel(context.channel.getId());
                options.save();

                context.channel.sendMessage("Welcome and Goodbye messages have been set to this channel.").queue();
                break;
            case 1:
                if (args[0].equalsIgnoreCase("clear")) {
                    options.setMessageChannel(null);
                    options.setWelcomeEnable(false);
                    options.setGoodbyeEnable(false);
                    options.save();

                    context.channel.sendMessage("Disabling Welcome and Goodbye messages and clearing channel.").queue();
                } else
                    context.invalid(this);
                break;
        }
    }
}