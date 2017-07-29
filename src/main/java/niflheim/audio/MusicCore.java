package niflheim.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.beam.BeamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MusicCore {
    private static final int DEFAULT_VOLUME = 35;
    private final AudioPlayerManager playerManager;
    private final HashMap<String, GuildMusicManager> musicManagers;

    public MusicCore() {
        playerManager = new DefaultAudioPlayerManager();
        musicManagers = new HashMap<>();

        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
        playerManager.registerSourceManager(new VimeoAudioSourceManager());
        playerManager.registerSourceManager(new BandcampAudioSourceManager());
        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        playerManager.registerSourceManager(new BeamAudioSourceManager());
    }

    public void loadAndPlay(GuildMusicManager mng, TextChannel channel, String url, final boolean addPlaylist) {
        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN).setTitle("Music Player");
        final String trackUrl;

        if (url.startsWith("<") && url.endsWith(">"))
            trackUrl = url.substring(1, url.length() - 1);
        else
            trackUrl = url;

        playerManager.loadItemOrdered(mng, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                String msg = "Successfully queued [" + track.getInfo().title + "](" + track.getInfo().uri + ") to the Music Queue.";

                if (mng.player.getPlayingTrack() == null)
                    msg = "Started playing [" + track.getInfo().title + "](" + track.getInfo().uri + ")";

                embed.setDescription(msg);

                mng.scheduler.queue(track);
                channel.sendMessage(embed.build()).queue(s -> selfDelete(channel, s));
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack first = playlist.getSelectedTrack();
                List<AudioTrack> tracks = playlist.getTracks();

                if (first == null)
                    first = playlist.getTracks().get(0);

                if (addPlaylist) {
                    embed.setDescription("Successfully queued " + playlist.getTracks().size() + " tracks from playlist " + playlist.getName() + " to the Music Queue.");
                    channel.sendMessage(embed.build()).queue(s -> selfDelete(channel, s));
                    tracks.forEach(mng.scheduler::queue);
                } else {
                    embed.setDescription("Successfully queued [" + first.getInfo().title + "](" + first.getInfo().uri + ") from playlist " + playlist.getName() + " to the Music Queue.");
                    channel.sendMessage(embed.build()).queue(s -> selfDelete(channel, s));
                    mng.scheduler.queue(first);
                }
            }

            @Override
            public void noMatches() {
                embed.setDescription("Nothing found by: " + trackUrl);
                channel.sendMessage(embed.build()).queue(s -> selfDelete(channel, s));
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                embed.setDescription("Could not play: " + exception.getMessage());
                channel.sendMessage(embed.build()).queue(s -> selfDelete(channel, s));
            }
        });
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        String id = guild.getId();
        GuildMusicManager mng = musicManagers.get(id);

        if (mng == null) {
            synchronized (musicManagers) {
                mng = musicManagers.get(id);
                if (mng == null) {
                    mng = new GuildMusicManager(playerManager);
                    mng.player.setVolume(DEFAULT_VOLUME);
                    musicManagers.put(id, mng);
                }
            }
        }

        return mng;
    }

    public AudioPlayerManager getPlayerManager() {
        return playerManager;
    }

    public HashMap<String, GuildMusicManager> getMusicManagers() {
        return musicManagers;
    }

    private void selfDelete(TextChannel channel, Message message) {
        if (channel.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_MANAGE))
            message.delete().queueAfter(5, TimeUnit.SECONDS);
    }
}
