package niflheim.commands.general;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        help = "Provides Okita invite link.",
        usage = ".invite",
        cooldown = 3000L,
        category = Category.GENERAL,
        scope = Scope.GUILD
)
public class Invite extends Command {
    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN)
                .setAuthor("Invite me!", null, context.jda.getSelfUser().getEffectiveAvatarUrl())
                .setDescription("[Use this link to invite me to your server!](https://discordapp.com/oauth2/authorize?client_id=298963480042668032&scope=bot&permissions=-1)");

        context.channel.sendMessage(embed.build()).queue();
    }
}