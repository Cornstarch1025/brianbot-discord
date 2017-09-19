package niflheim.commands.music;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;
import niflheim.Okita;
import niflheim.audio.GuildMusicManager;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        name = "Join",
        example = ".join Music",
        aliases = "connect",
        help = "Joins a voice channel.",
        usage = ".join <Channel>",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.VOICE,
        permissions = {Permission.VOICE_CONNECT}
)
public class Join extends Command {
    public void execute(Context context, String[] args) {
        if (args.length == 0) {
            context.invalid(this);
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN).setTitle("Music Player");
        StringBuilder channel = new StringBuilder();

        for (String x : args)
            channel.append(x).append(" ");

        channel.deleteCharAt(channel.lastIndexOf(" "));

        GuildMusicManager manager = Okita.musicCore.getMusicManager(context.guild);
        VoiceChannel vc = null;

        if (context.guild.getVoiceChannelsByName(channel.toString(), true).size() > 0)
            vc = context.guild.getVoiceChannelsByName(channel.toString(), true).get(0);
        else {
            context.channel.sendMessage("Voice Channel not found!").queue();
            return;
        }

        if (vc != null && manager.player.getPlayingTrack() == null)
            try {
                if (context.guild.getAudioManager().getSendingHandler() == null)
                    context.guild.getAudioManager().setSendingHandler(manager.getSendHandler());

                embed.setDescription("Joining channel " + vc.getName());

                context.channel.sendMessage(embed.build()).queue();
                context.guild.getAudioManager().openAudioConnection(vc);
            } catch (PermissionException e) {
                context.channel.sendMessage("I do not have permission to connect!").queue();
            }
        else
            context.channel.sendMessage("I'm currently streaming music!").queue();
    }
}