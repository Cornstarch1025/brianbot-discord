package niflheim.commands.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.PermissionException;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        help = "Temporarily bans one member from the guild for a set amount of days.",
        usage = ".tempban <# Days> <@Member>",
        cooldown = 3000L,
        category = Category.MOD,
        scope = Scope.GUILD,
        permissions = {Permission.BAN_MEMBERS}
)
public class TempBan extends Command {
    public void execute(Context context, String[] args) {
        if (context.message.getMentionedUsers().size() != 1) {
            context.channel.sendMessage("Please mention **one** member!").queue();
            return;
        }

        int days = 0;

        try {
            days = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            context.invalid(this);
            return;
        }

        User target = context.message.getMentionedUsers().get(0);

        try {
            context.guild.getController().ban(target, days).queue(s -> context.guild.getController().unban(target).queue());
        } catch (PermissionException e) {
            context.channel.sendMessage("You are unable to ban " + target.getAsMention()).queue();
        }
    }
}