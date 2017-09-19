package niflheim.commands.admin;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        name = "Msg",
        aliases = "message",
        help = "Messages a User in Okita's directory.",
        usage = ".msg <User ID> <Message>",
        example = ".msg 191410544278765568 Hello!",
        admin = true,
        category = Category.ADMIN
)
public class Msg extends Command {
    private EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN);

    @Override
    public void execute(Context context, String[] args) {
        embed.setAuthor("Message from " + context.user.getName(), null, context.user.getEffectiveAvatarUrl());

        StringBuilder message = new StringBuilder();

        switch (args.length) {
            default:
                for (String x : args)
                    message.append(x).append(" ");

                embed.setDescription(message.toString())
                        .setFooter(context.time(), null);

                if (Okita.getAllUsersAsMap().get(args[0]) != null)
                    Okita.getAllUsersAsMap().get(args[0]).openPrivateChannel().queue(s -> s.sendMessage(embed.build()).queue(c -> context.channel.sendMessage("Message has been successfully sent.").queue()));
                else
                    context.channel.sendMessage("User not found!").queue();
                break;
            case 0:
                context.invalid(this);
                break;
            case 1:
                context.invalid(this);
                break;
        }
    }
}
