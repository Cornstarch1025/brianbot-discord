package niflheim.commands.info;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;
import niflheim.utils.Settings;

import java.awt.*;

@CommandFrame(
        aliases = {"guild"},
        help = "Displays server basic information.",
        usage = ".server",
        cooldown = 3000L,
        category = Category.INFO,
        scope = Scope.GUILD
)
public class Server extends Command {
    public void execute(Context context, String[] args) {
        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN);

        switch (args.length) {
            default:
                context.invalid(this);
                break;
            case 0:
                embed.clearFields()
                        .setTitle(context.guild.getName(), null)
                        .setThumbnail(context.guild.getIconUrl())
                        .setDescription("Guild ID: " + context.guild.getId())
                        .addField("Members[" + context.guild.getMembers().size() + "]", getOnline(context.guild), true)
                        .addField("Region", context.guild.getRegion().toString(), true)
                        .addField("Channels[" + (context.guild.getTextChannels().size() + context.guild.getVoiceChannels().size()) + "]", context.guild.getTextChannels().size() + " Text and " + context.guild.getVoiceChannels().size() + " Voice", true)
                        .addField("Default Channel", context.guild.getPublicChannel().getAsMention(), true)
                        .addField("Server Owner", context.guild.getOwner().getUser().getName() + "#" + context.guild.getOwner().getUser().getDiscriminator(), false)
                        .addField("Server Roles[" + context.guild.getRoles().size() + "]", "Run " + Settings.PREFIX + "server roles to get a list of all server  roles", false)
                        .setFooter(context.time(), null);

                context.channel.sendMessage(embed.build()).queue();
                break;
            case 1:
                if (args[0].equalsIgnoreCase("roles")) {
                    embed.clearFields()
                            .setTitle("Server Roles", null)
                            .setThumbnail(context.guild.getIconUrl())
                            .setDescription(getRoles(context.guild))
                            .setFooter(context.time(), null);
                    context.channel.sendMessage(embed.build()).queue();
                } else
                    context.invalid(this);
                break;
        }
    }

    private String getRoles(Guild guild) {
        StringBuilder str = new StringBuilder();

        for (Role x : guild.getRoles())
            str.append(x.getName() + ", ");

        str.deleteCharAt(str.lastIndexOf(","));

        return str.toString();
    }

    private String getOnline(Guild guild) {
        int count = 0;

        for (Member x : guild.getMembers())
            if (x.getOnlineStatus().toString().toLowerCase().startsWith("on"))
                count++;

        return Integer.toString(count) + " member(s) online";
    }
}