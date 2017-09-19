package niflheim.commands.mod;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;
import niflheim.rethink.GuildOptions;

@CommandFrame(
        name = "Welcome",
        example = ".welcome Has joined the server!",
        help = "Sets the guild's welcome message for whenever a member joins.",
        usage = ".welcome <Message>, .welcome clear, .welcome enable",
        cooldown = 5000L,
        guildOwner = true,
        category = Category.MOD,
        scope = Scope.GUILD
)
public class Welcome extends Command {
    public void execute(Context context, String[] args) {
        GuildOptions options = context.guildOptions;

        switch (args.length) {
            default:
                StringBuilder str = new StringBuilder();

                for (String x : args)
                    str.append(x).append(" ");

                options.setWelcome(str.toString());
                if (options.getMessageChannel() == null)
                    options.setMessageChannel(context.channel.getId());
                options.setWelcomeEnable(true);
                options.save();

                context.channel.sendMessage("Guild welcome message has been set and enabled.").queue();
                break;
            case 0:
                if (options.getWelcome() == null)
                    context.channel.sendMessage("Guild welcome message not set!").queue();
                else
                    context.channel.sendMessage("Current guild welcome message: " + options.getWelcome()).queue();
                break;
            case 1:
                if (args[0].equalsIgnoreCase("clear")) {
                    options.setWelcome(null);
                    options.setWelcomeEnable(false);
                    options.save();
                    context.channel.sendMessage("Guild welcome message has been cleared and disabled.").queue();
                } else if (args[0].equalsIgnoreCase("enable")) {
                    if (options.getWelcome() == null) {
                        context.channel.sendMessage("Can't enable welcome message because none set!").queue();
                        return;
                    }

                    options.setWelcomeEnable(true);
                    options.save();

                    context.channel.sendMessage("Welcome messages have been enabled.").queue();
                } else
                    context.invalid(this);
                break;
        }
    }
}