package niflheim.commands.admin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        name = "Search",
        example = ".search Niflheim",
        help = "Searches for a User in Okita's directory.",
        usage = ".search <Username>",
        admin = true,
        toggleable = false,
        category = Category.ADMIN
)
public class Search extends Command {
    private EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN);

    @Override
    public void execute(Context context, String[] args) {
        if (args.length == 0) {
            context.invalid(this);
            return;
        }

        StringBuilder name = new StringBuilder();
        StringBuilder result = new StringBuilder();

        for (String str : args)
            name.append(str).append(" ");

        name.deleteCharAt(name.length() - 1);

        for (User usr : Okita.getAllUsersAsMap().values())
            if (usr.getName().equalsIgnoreCase(name.toString()))
                result.append(usr.getName()).append("#").append(usr.getDiscriminator()).append(" `").append(usr.getId()).append("`\n");

        if (result.length() == 0)
            result.append("No matching users found!");

        embed.setAuthor("User Search", null, context.user.getAvatarUrl())
                .setDescription(result.toString())
                .setFooter(context.time(), null);

        context.channel.sendMessage(embed.build()).queue();
    }
}
