package niflheim.commands.music;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        help = "Shuffles the queue.",
        usage = ".shuffle",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.GUILD
)
public class Shuffle extends Command {
    EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN).setTitle("Music Player").setDescription("The queue has been shuffled.");

    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        if(!context.guild.getAudioManager().isConnected())
            context.channel.sendMessage("I am not connected to a Voice Channel!").queue();
        else
            context.channel.sendMessage(embed.build()).queue(s -> Okita.musicCore.getMusicManager(context.guild).scheduler.shuffle());
    }
}