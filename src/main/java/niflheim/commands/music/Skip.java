package niflheim.commands.music;

import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        help = "Skips the currently playing song.",
        usage = ".skip",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.VOICE
)
public class Skip extends Command {
    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        if(!context.guild.getAudioManager().isConnected())
            context.channel.sendMessage("I am not connected to a Voice Channel!").queue();
        else
            Okita.musicCore.getMusicManager(context.guild).scheduler.skipTrack();
    }
}