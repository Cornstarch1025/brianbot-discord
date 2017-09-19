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
        name = "Kick",
        example = ".kick @Niflheim",
        help = "Kicks all mentioned users from the server.",
        usage = ".kick <@Members...>",
        cooldown = 3000L,
        category = Category.MOD,
        scope = Scope.GUILD,
        permissions = {Permission.KICK_MEMBERS}
)
public class Kick extends Command {
    public void execute(Context context, String[] args) {
        if (context.message.getMentionedUsers().size() == 0) {
            context.channel.sendMessage("Please mention at least one member!").queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN).setAuthor("Kick Summary", null, context.user.getEffectiveAvatarUrl());
        StringBuilder message = new StringBuilder();

        context.message.getMentionedUsers().stream().forEach(u -> {
            try {
                context.guild.getController().kick(context.guild.getMember(u)).queue();
            } catch (PermissionException e) {
                message.append(u.getName()).append(", ");
            }
        });

        if (message.length() != 0)
            message.deleteCharAt(message.lastIndexOf(", ")).append(".").insert(0, "Failed to kick ");
        else
            message.append("No failed kicks!");

        context.channel.sendMessage(embed.setDescription(message.toString()).build()).queue();
    }
}