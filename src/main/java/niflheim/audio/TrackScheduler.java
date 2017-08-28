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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final Queue<AudioTrack> queue;
    private TextChannel activeChannel;
    private boolean repeat;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList<>();
        repeat = false;
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true))
            queue.offer(track);
    }

    public void nextTrack(AudioTrack old) {
        AudioTrack track = queue.poll();
        String content = old.getInfo().title + " has finished playing. " + (track != null ? "Now playing " + track.getInfo().title + "." : "The Music Queue has concluded.");

        if(repeat)
            track = old.makeClone();

        player.startTrack(track, false);

        if(!repeat)
            sendMessage(content);
    }

    public void skipTrack() {
        repeat = false;

        AudioTrack track = queue.poll();
        player.startTrack(track, false);

        String content = player.getPlayingTrack().getInfo().title + " has been skipped. " + (track != null ? "Now playing " + track.getInfo().title + "." : "The Music Queue has concluded.");
        sendMessage(content);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason reason) {
        if (reason.mayStartNext)
            nextTrack(track);
    }

    public void setActiveChannel(TextChannel activeChannel) {
        this.activeChannel = activeChannel;
    }

    public void setRepeat(boolean toggle) {
        repeat = toggle;
    }

    public Queue<AudioTrack> getQueue() {
        return queue;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void shuffle() {
        Collections.shuffle((List<?>) queue);
    }

    public void clearQueue() {
        queue.clear();
    }

    private MessageEmbed trackEmbed(String content) {
        return new EmbedBuilder().setColor(Color.CYAN)
                .setTitle("Music Player")
                .setDescription(content)
                .build();
    }

    private void sendMessage(String content) {
        if (activeChannel != null)
            activeChannel.sendMessage(trackEmbed(content)).queue(m -> {
                if (activeChannel.getGuild().getSelfMember().hasPermission(activeChannel, Permission.MESSAGE_MANAGE))
                    m.delete().queueAfter(5, TimeUnit.SECONDS);
            });
    }
}
