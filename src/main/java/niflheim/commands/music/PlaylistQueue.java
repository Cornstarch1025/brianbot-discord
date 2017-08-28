package niflheim.commands.music;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.exceptions.PermissionException;
import niflheim.Okita;
import niflheim.audio.GuildMusicManager;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.util.concurrent.TimeUnit;

@CommandFrame(
        aliases = {"pq"},
        help = "Queues a playlist from a URL.",
        usage = ".pq <Url>",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.GUILD
)
public class PlaylistQueue extends Command {
    public void execute(Context context, String[] args) {
        if (args.length != 1) {
            context.invalid(this);
            return;
        }

        GuildMusicManager manager = Okita.musicCore.getMusicManager(context.guild);
        manager.setChannel(context.channel);

        if (context.guild.getAudioManager().getConnectedChannel() == null)
            try {
                if (context.guild.getAudioManager().getSendingHandler() == null)
                    context.guild.getAudioManager().setSendingHandler(manager.getSendHandler());

                context.guild.getAudioManager().openAudioConnection(context.member.getVoiceState().getChannel());
            } catch (PermissionException e) {
                context.channel.sendMessage("I do not have permission to connect!").queue();
                return;
            }
        else if (context.guild.getAudioManager().getConnectedChannel() != context.member.getVoiceState().getChannel()) {
            context.channel.sendMessage("Please enter connected Voice Channel.").queue();
            return;
        }

        Okita.musicCore.loadAndPlay(manager, context.channel, args[0], true);
        if (context.guild.getSelfMember().hasPermission(context.channel, Permission.MESSAGE_MANAGE))
            context.message.delete().queueAfter(5, TimeUnit.SECONDS);
    }
}