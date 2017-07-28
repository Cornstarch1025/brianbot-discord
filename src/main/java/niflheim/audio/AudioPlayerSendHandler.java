package niflheim.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer player;
    private AudioFrame last;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        player = audioPlayer;
    }

    @Override
    public boolean canProvide() {
        if (last == null)
            last = player.provide();

        return last != null;
    }

    @Override
    public byte[] provide20MsAudio() {
        if (last == null)
            last = player.provide();

        byte[] data = last != null ? last.data : null;
        last = null;

        return data;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
