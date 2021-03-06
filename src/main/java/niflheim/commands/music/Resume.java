package niflheim.commands.music;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        name = "Resume",
        example = ".resume",
        help = "Resumes the music player.",
        usage = ".resume",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.VOICE
)
public class Resume extends Command {
    private MessageEmbed embed = new EmbedBuilder().setColor(Color.CYAN).setTitle("Music Player").setDescription("The player has resumed.").build();

    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        if (!context.guild.getAudioManager().isConnected()) {
            context.channel.sendMessage("I am not connected to a Voice Channel!").queue();
            return;
        }

        if (Okita.musicCore.getMusicManager(context.guild).player.getPlayingTrack() != null) {
            if (Okita.musicCore.getMusicManager(context.guild).player.isPaused()) {
                Okita.musicCore.getMusicManager(context.guild).player.setPaused(false);
                context.channel.sendMessage(embed).queue();
            } else
                context.channel.sendMessage("Music player is not paused!").queue();
        } else
            context.channel.sendMessage("I am not playing anything!").queue();
    }
}