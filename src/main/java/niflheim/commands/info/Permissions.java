package niflheim.commands.info;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        name = "Permissions",
        example = ".permissions channel",
        aliases = {"perms"},
        help = "Lists a members permissions guild or channel wide.",
        usage = ".perms, .perms channel",
        cooldown = 3000L,
        category = Category.INFO,
        scope = Scope.GUILD
)
public class Permissions extends Command {
    private EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN);

    @Override
    public void execute(Context context, String[] args) {
        switch (args.length) {
            default:
                context.invalid(this);
                break;
            case 0:
                embed.setAuthor(context.user.getName() + "'s Permissions", null, context.user.getEffectiveAvatarUrl())
                        .setDescription(getPerms(context.member, null))
                        .setFooter(context.time(), null);

                context.channel.sendMessage(embed.build()).queue();
                break;
            case 1:
                if (!"channels".contains(args[0].toLowerCase())) {
                    context.invalid(this);
                    return;
                }

                embed.setAuthor(context.user.getName() + "'s Permissions in " + context.channel.getName(), null, context.user.getEffectiveAvatarUrl())
                        .setDescription(getPerms(context.member, context.channel))
                        .setFooter(context.time(), null);

                context.channel.sendMessage(embed.build()).queue();
                break;
        }
    }

    private String getPerms(Member member, TextChannel channel) {
        StringBuilder perms = new StringBuilder();

        if (channel != null)
            for (Permission x : member.getPermissions(channel))
                perms.append(x.getName()).append(", ");
        else
            for (Permission y : member.getPermissions())
                perms.append(y.getName()).append(", ");

        if (perms.length() == 0)
            return "No permissions!";
        else
            return perms.substring(0, perms.length() - 2);
    }
}