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
        help = "Repeats current track until toggled off.",
        usage = ".repeat",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.VOICE
)
public class Repeat extends Command {
    public void execute(Context context, String[] args) {
        if(args.length != 0){
            context.invalid(this);
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN).setTitle("Music Player");
        GuildMusicManager manager = Okita.musicCore.getMusicManager(context.guild);

        if(!context.guild.getAudioManager().isConnected()) {
            context.channel.sendMessage("I am not connected to a Voice Channel!").queue();
            return;
        }

        if(manager.player.getPlayingTrack() == null){
            context.channel.sendMessage("I am not playing anything!").queue();
            return;
        }

        if(!manager.scheduler.isRepeat()){
            manager.scheduler.setRepeat(true);
            embed.setDescription("Player has been set to repeat.");
        } else
            embed.setDescription("Player is already on repeat!");

        context.channel.sendMessage(embed.build()).queue();
    }
}