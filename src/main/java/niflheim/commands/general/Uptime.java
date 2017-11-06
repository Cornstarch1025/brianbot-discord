package niflheim.commands.general;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;
import java.lang.management.ManagementFactory;

@CommandFrame(
        name = "Uptime",
        example = ".uptime",
        help = "Displays Automata's uptime.",
        usage = ".uptime",
        cooldown = 3000L,
        category = Category.GENERAL,
        scope = Scope.GUILD
)
public class Uptime extends Command {
    public void execute(Context context, String[] args) {
        if (args.length != 0){
            context.invalid(this);
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN);

        long ss = ManagementFactory.getRuntimeMXBean().getUptime();
        long s = (ss / 1000) % 60;
        long m = (ss / (1000 * 60)) % 60;
        long h = (ss / (1000 * 60 * 60)) % 24;
        long d = (ss / (1000 * 60 * 60 * 24)) % 365;

        embed.setAuthor("Automata's Uptime", null, context.jda.getSelfUser().getEffectiveAvatarUrl())
                .setDescription("Online for: " + Long.toString(d) + "d " + Long.toString(h) + "h " + Long.toString(m) + "m " + Long.toString(s) + "s");

        context.channel.sendMessage(embed.build()).queue();
    }
}