package niflheim.commands.mod;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;
import niflheim.rethink.GuildOptions;

@CommandFrame(
        help = "Sets the guild's goodbye message for whenever a member leaves.",
        usage = ".goodbye <Message>, .goodbye clear, .goodbye enable",
        cooldown = 5000L,
        guildOwner = true,
        category = Category.MOD,
        scope = Scope.GUILD
)
public class Goodbye extends Command {
    public void execute(Context context, String[] args) {
        GuildOptions options = context.guildOptions;

        switch (args.length) {
            default:
                StringBuilder str = new StringBuilder();

                for (String x : args)
                    str.append(x).append(" ");

                options.setGoodbye(str.toString());
                if (options.getMessageChannel() == null)
                    options.setMessageChannel(context.channel.getId());
                options.setGoodbyeEnable(true);
                options.save();

                context.channel.sendMessage("Guild goodbye message has been set and enabled.").queue();
                break;
            case 0:
                if (options.getGoodbye() == null)
                    context.channel.sendMessage("Guild goodbye message not set!").queue();
                else
                    context.channel.sendMessage("Current guild goodbye message: " + options.getGoodbye()).queue();
                break;
            case 1:
                if (args[0].equalsIgnoreCase("clear")) {
                    options.setGoodbye(null);
                    options.setGoodbyeEnable(false);
                    options.save();
                    context.channel.sendMessage("Guild goodbye message has been cleared and disabled.").queue();
                } else if (args[0].equalsIgnoreCase("enable")) {
                    if (options.getGoodbye() == null) {
                        context.channel.sendMessage("Can't enable goodbye message because none set!").queue();
                        return;
                    }

                    options.setGoodbyeEnable(true);
                    options.save();

                    context.channel.sendMessage("Goodbye messages have been enabled.").queue();
                } else
                    context.invalid(this);
                break;
        }
    }
}