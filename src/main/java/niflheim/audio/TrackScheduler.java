package niflheim.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final Queue<AudioTrack> queue;
    private TextChannel activeChannel;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList<>();
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true))
            queue.offer(track);
    }

    public void nextTrack(String old) {
        AudioTrack track = queue.poll();
        player.startTrack(track, false);

        String content = old + " has finished playing. " + (track != null ? "Now playing " + track.getInfo().title + "." : "The Music Queue has concluded.");

        if (activeChannel != null)
            activeChannel.sendMessage(trackEmbed(content)).queue(m -> {
                if (activeChannel.getGuild().getSelfMember().hasPermission(activeChannel, Permission.MESSAGE_MANAGE))
                    m.delete().queueAfter(5, TimeUnit.SECONDS);
            });
    }

    public void skipTrack() {
        AudioTrack track = queue.poll();
        player.startTrack(track, false);

        String content = player.getPlayingTrack().getInfo().title + " has been skipped. " + (track != null ? "Now playing " + track.getInfo().title + "." : "The Music Queue has concluded.");

        if (activeChannel != null)
            activeChannel.sendMessage(trackEmbed(content)).queue(m -> {
                if (activeChannel.getGuild().getSelfMember().hasPermission(activeChannel, Permission.MESSAGE_MANAGE))
                    m.delete().queueAfter(5, TimeUnit.SECONDS);
            });
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason reason) {
        if (reason.mayStartNext)
            nextTrack(track.getInfo().title);
    }

    public void setActiveChannel(TextChannel activeChannel) {
        this.activeChannel = activeChannel;
    }

    private MessageEmbed trackEmbed(String content) {
        return new EmbedBuilder().setColor(Color.CYAN)
                .setTitle("Music Player")
                .setDescription(content)
                .build();
    }
}
