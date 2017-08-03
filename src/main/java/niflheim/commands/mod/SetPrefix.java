package niflheim.commands.mod;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;
import niflheim.utils.Settings;

@CommandFrame(
        help = "Sets the guild prefix",
        usage = ".setprefix <prefix>",
        cooldown = 3000L,
        guildOwner = true,
        category = Category.MOD,
        scope = Scope.GUILD
)
public class SetPrefix extends Command {
    public void execute(Context context, String[] args) {
        switch (args.length) {
            default:
                context.channel.sendMessage("Prefix must not contain spaces!").queue();
                break;
            case 0:
                context.guildOptions.setPrefix(Settings.PREFIX);
                context.guildOptions.save();
                context.channel.sendMessage("No prefix specified so it was reset to `" + Settings.PREFIX + "`!").queue();
                break;
            case 1:
                context.guildOptions.setPrefix(args[0]);
                context.guildOptions.save();
                context.channel.sendMessage("Prefix has been set to `" + args[0] + "`!").queue();
                break;
        }
    }
}