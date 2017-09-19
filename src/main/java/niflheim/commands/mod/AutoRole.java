package niflheim.commands.mod;

import net.dv8tion.jda.core.Permission;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        name = "Autorole",
        example = ".autorole Member",
        help = "Sets the autorole for the guild.",
        usage = ".autorole <Role>",
        cooldown = 5000L,
        guildOwner = true,
        category = Category.MOD,
        scope = Scope.GUILD,
        permissions = {Permission.MANAGE_ROLES}
)
public class AutoRole extends Command {
    public void execute(Context context, String[] args) {
        if (args.length == 0) {
            String msg = context.guildOptions.getAutorole() == null ? "No autorole set for this guild!" : "Guild autorole is set to `" + context.guild.getRoleById(context.guildOptions.getAutorole()) + "`!";
            context.channel.sendMessage(msg).queue();
            return;
        }

        StringBuilder role = new StringBuilder();

        for (String x : args)
            role.append(x).append(" ");

        role.deleteCharAt(role.lastIndexOf(" "));

        switch (context.guild.getRolesByName(role.toString(), true).size()) {
            default:
                context.channel.sendMessage("Unable to set autorole, multiple roles with name `" + role.toString() + "`").queue();
                break;
            case 0:
                context.channel.sendMessage("No role found!").queue();
                break;
            case 1:
                context.guildOptions.setAutorole(context.guild.getRolesByName(role.toString(), true).get(0).getId());
                context.guildOptions.save();
                context.channel.sendMessage("Autorole has been set to `" + role.toString() + "`!").queue();
                break;
        }
    }
}