package niflheim.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.awt.*;

@CommandFrame(
        name = "NowPlaying",
        example = ".nowplaying",
        aliases = "np",
        help = "Shows currently playing song as well as position.",
        usage = ".np",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.VOICE
)
public class NowPlaying extends Command {
    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        if (!context.guild.getAudioManager().isConnected()) {
            context.channel.sendMessage("I am not connected to a Voice Channel!").queue();
            return;
        }

        AudioTrack track = Okita.musicCore.getMusicManager(context.guild).player.getPlayingTrack();

        if (track != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.CYAN)
                    .setTitle(track.getInfo().title, track.getInfo().uri)
                    .setDescription(progress(track))
                    .addField("Author", track.getInfo().author, true)
                    .setFooter(context.time(), null);

            context.channel.sendMessage(embed.build()).queue();
        } else
            context.channel.sendMessage("I am not playing anything!").queue();
    }

    public String progress(AudioTrack track) {
        long length = track.getDuration();
        long current = track.getPosition();

        StringBuilder str = new StringBuilder("[");

        for (int i = 0; i < (int) ((float) current / length * 20); i++)
            str.append("▬");

        str.append("]()");

        for (int j = (int) ((float) current / length * 20); j < 20; j++)
            str.append("▬");

        str.append(" `").append(DateFormatUtils.format(current, "mm:ss")).append("/").append(DateFormatUtils.format(length, "mm:ss")).append("`");

        return str.toString();
    }
}