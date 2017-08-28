package niflheim.commands.music;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.Okita;
import niflheim.audio.GuildMusicManager;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        help = "Stops the music player and clears the queue.",
        usage = ".stop",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.GUILD
)
public class Stop extends Command {
    private EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN).setTitle("Music Player").setDescription("Stopping music and clearing queue.");

    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        if (!context.guild.getAudioManager().isConnected())
            context.channel.sendMessage("I am not connected to a Voice Channel!").queue();
        else {
            context.channel.sendMessage(embed.build()).queue(s -> {
                GuildMusicManager mng = Okita.musicCore.getMusicManager(context.guild);
                mng.scheduler.clearQueue();
                mng.player.stopTrack();
            });
        }
    }
}