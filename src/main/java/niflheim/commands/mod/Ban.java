package niflheim.commands.mod;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.exceptions.PermissionException;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        name = "Ban",
        example = ".ban @Niflheim",
        help = "Bans all mentioned users from the server.",
        usage = ".ban <@Members...>",
        cooldown = 3000L,
        category = Category.MOD,
        scope = Scope.GUILD,
        permissions = {Permission.BAN_MEMBERS}
)
public class Ban extends Command {
    public void execute(Context context, String[] args) {
        if (context.message.getMentionedUsers().size() == 0) {
            context.channel.sendMessage("Please mention at least one member!").queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN).setAuthor("Ban Summary", null, context.user.getEffectiveAvatarUrl());
        StringBuilder message = new StringBuilder();

        context.message.getMentionedUsers().stream().forEach(u -> {
            try {
                context.guild.getController().ban(u, 0).queue();
            } catch (PermissionException e) {
                message.append(u.getName()).append(", ");
            }
        });

        if (message.length() != 0)
            message.deleteCharAt(message.lastIndexOf(", ")).append(".").insert(0, "Failed to ban ");
        else
            message.append("No failed bans!");

        context.channel.sendMessage(embed.setDescription(message.toString()).build()).queue();
    }
}