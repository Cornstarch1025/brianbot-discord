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
        aliases = "disconnect",
        help = "Disconnects from a voice channel.",
        usage = ".leave",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.GUILD
)
public class Leave extends Command {
    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN).setTitle("Music Player");

        if (context.guild.getAudioManager().isConnected()) {
            embed.setDescription("Stopping playback and leaving " + context.guild.getAudioManager().getConnectedChannel().getName());

            context.channel.sendMessage(embed.build()).queue(s -> {
                context.guild.getAudioManager().closeAudioConnection();
                Okita.musicCore.destroy(context.guild);
            });
        } else
            context.channel.sendMessage("I am not connected to a Voice Channel!").queue();
    }
}