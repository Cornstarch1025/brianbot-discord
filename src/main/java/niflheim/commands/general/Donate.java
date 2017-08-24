package niflheim.commands.general;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        aliases = {"premium"},
        help = "Displays donation links as well as a list of donators.",
        usage = ".donate",
        cooldown = 3000L,
        category = Category.GENERAL,
        scope = Scope.GUILD
)
public class Donate extends Command {
    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN)
                .setAuthor("Donate!", null, context.jda.getSelfUser().getEffectiveAvatarUrl())
                .setDescription("Server hosting isn't free! Please consider supporting us and unlocking premium features!")
                .addField("Patreon", "[Patreon Link](https://www.patreon.com/OkitaBot)", true)
                .addField("Paypal", "[Paypal Link](https://www.paypal.me/Niflheim)", true);

        context.channel.sendMessage(embed.build()).queue();
    }
}