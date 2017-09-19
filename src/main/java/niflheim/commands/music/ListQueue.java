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
import java.util.Queue;

@CommandFrame(
        name = "ListQueue",
        example = ".listqueue 1",
        aliases = {"lq"},
        help = "Lists all queued songs as well as currently playing song.",
        usage = ".lq, .lq <Page>",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.VOICE
)
public class ListQueue extends Command {
    public void execute(Context context, String[] args) {
        if (args.length > 1) {
            context.invalid(this);
            return;
        }

        if(!context.guild.getAudioManager().isConnected()) {
            context.channel.sendMessage("I am not connected to a Voice Channel!").queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN).setTitle("Music Queue");
        Queue<AudioTrack> tracks = Okita.musicCore.getMusicManager(context.guild).scheduler.getQueue();

        if (tracks.size() == 0)
            embed.clearFields()
                    .setDescription("No tracks in queue!")
                    .setFooter(context.time(), null);
        else {
            StringBuilder name = new StringBuilder();

            int position = 1;

            for (AudioTrack track : tracks) {
                name.append("**").append(position).append(".** ").append("`[").append(DateFormatUtils.format(track.getDuration(), "mm:ss")).append("]` ").append(track.getInfo().title).append("\n");
                position++;
            }

            int page = 1;

            if (args.length == 1)
                try {
                    page = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    context.invalid(this);
                    return;
                }

            if (page < 0 || (page > 1 && page > (int) (Math.ceil(tracks.size() / 10.00)))) {
                context.channel.sendMessage("Not a valid page!").queue();
                return;
            } else {
                String[] trackNames = name.toString().split("\n");
                StringBuilder names2 = new StringBuilder();
                int count = (10 * (page - 1)) + 1;

                while (count <= (page) * 10 && count <= trackNames.length) {
                    names2.append(trackNames[count - 1]).append("\n");
                    count++;
                }

                embed.clearFields()
                        .setDescription("Page " + page + "/" + (int) (Math.ceil(tracks.size() / 10.00)) + ". " + tracks.size() + " tracks in queue.")
                        .addField("Tracks", names2.toString(), false)
                        .setFooter(context.time(), null);
            }
        }
        context.channel.sendMessage(embed.build()).queue();
    }
}