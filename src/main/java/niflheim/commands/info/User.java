package niflheim.commands.info;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        aliases = {"member"},
        help = "Displays basic information on a User.",
        usage = ".user <User>",
        cooldown = 3000L,
        category = Category.INFO,
        scope = Scope.GUILD
)
public class User extends Command {
    public void execute(Context context, String[] args) {
        switch (args.length) {
            default:
                Member target = null;
                StringBuilder str = new StringBuilder();

                for (String x : args)
                    str.append(x).append(" ");

                str.deleteCharAt(str.length() - 1);

                if (context.message.getMentionedUsers().size() != 0)
                    target = context.guild.getMember(context.message.getMentionedUsers().get(0));
                else if (context.guild.getMembersByNickname(str.toString(), true).size() != 0)
                    target = context.guild.getMembersByNickname(str.toString(), true).get(0);
                else if (context.guild.getMembersByName(str.toString(), true).size() != 0)
                    target = context.guild.getMembersByName(str.toString(), true).get(0);
                else {
                    context.channel.sendMessage("User not found!").queue();
                    return;
                }

                context.channel.sendMessage(embedBuilder(target, target.getUser(), context.time()).build()).queue();
                break;
            case 0:
                context.channel.sendMessage(embedBuilder(context.member, context.user, context.time()).build()).queue();
                break;
        }
    }

    private String getUserRoles(Member member) {
        StringBuilder roles = new StringBuilder();

        for (Role x : member.getRoles())
            roles.append(x.getName() + ", ");

        if (roles.length() > 0)
            roles.deleteCharAt(roles.lastIndexOf(","));

        return roles.toString();
    }

    private String getNickname(Member member) {
        String nick = "No nickname set!";

        if (member.getNickname() != null)
            nick = member.getNickname();

        return nick;
    }

    private EmbedBuilder embedBuilder(Member member, net.dv8tion.jda.core.entities.User user, String time) {
        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN);

        embed.clearFields()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .addField("User ID", user.getId(), true)
                .addField("Status", member.getOnlineStatus().toString().substring(0, 1) + member.getOnlineStatus().toString().toLowerCase().substring(1), true)
                .addField("Nickname", getNickname(member), false)
                .addField("Roles[" + member.getRoles().size() + "]", getUserRoles(member), false)
                .setFooter(time, null)
                .setThumbnail(user.getEffectiveAvatarUrl());

        return embed;
    }
}