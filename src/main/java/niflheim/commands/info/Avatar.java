package niflheim.commands.info;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        name = "Avatar",
        example = ".avatar Niflheim",
        help = "Displays a users avatar.",
        usage = ".avatar, .avatar <User>",
        cooldown = 3000L,
        category = Category.INFO,
        scope = Scope.GUILD
)
public class Avatar extends Command {
    private EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN);

    @Override
    public void execute(Context context, String[] args) {
        switch (args.length) {
            default:
                StringBuilder str = new StringBuilder();
                User target = null;

                for (String x : args)
                    str.append(x).append(" ");

                str.deleteCharAt(str.length() - 1);

                if (context.message.getMentionedUsers().size() == 1)
                    target = context.message.getMentionedUsers().get(0);
                else if (context.guild.getMembersByEffectiveName(str.toString(), true).size() != 0)
                    target = context.guild.getMembersByEffectiveName(str.toString(), true).get(0).getUser();
                else if (context.guild.getMembersByName(str.toString(), true).size() != 0)
                    target = context.guild.getMembersByName(str.toString(), true).get(0).getUser();
                else {
                    context.channel.sendMessage("User not found!").queue();
                    return;
                }

                context.channel.sendMessage(embedBuild(target).setFooter(context.time(), null).build()).queue();
                break;
            case 0:
                context.channel.sendMessage(embedBuild(context.user).setFooter(context.time(), null).build()).queue();
                break;
        }
    }

    private EmbedBuilder embedBuild(User user) {
        return embed.clearFields()
                .setAuthor(user.getName() + "'s Avatar", null, user.getEffectiveAvatarUrl())
                .setImage(user.getEffectiveAvatarUrl());
    }
}