package niflheim.commands.general;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        help = "Provides the Support Server link.",
        usage = ".support",
        cooldown = 3000L,
        category = Category.GENERAL,
        scope = Scope.GUILD
)
public class Support extends Command {
    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN)
                .setAuthor("Support Server!", null, context.jda.getSelfUser().getEffectiveAvatarUrl())
                .setDescription("[Use this link to join the Support Server!](https://discord.gg/DC5PzXN)");

        context.channel.sendMessage(embed.build()).queue();
    }
}