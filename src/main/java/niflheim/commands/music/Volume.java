package niflheim.commands.music;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        help = "Changes the music player volume.",
        usage = ".volume, .volume <0-100>",
        cooldown = 3000L,
        category = Category.MUSIC,
        scope = Scope.VOICE
)
public class Volume extends Command {
    public void execute(Context context, String[] args) {
        if (!context.guild.getAudioManager().isConnected()) {
            context.channel.sendMessage("I am not connected to a Voice Channel!").queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN).setTitle("Music Player");
        float v = Okita.musicCore.getMusicManager(context.guild).player.getVolume();

        switch (args.length) {
            default:
                context.invalid(this);
                break;
            case 0:
                context.channel.sendMessage(embed.setDescription(description(v)).setFooter("Volume is currently " + (int) v, null).build()).queue();
                break;
            case 1:
                float v2;

                try {
                    v2 = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    context.invalid(this);
                    return;
                }

                if (v2 < 0 || v2 > 100)
                    context.channel.sendMessage("Please enter a valid volume!").queue();
                else
                    context.channel.sendMessage(embed.setDescription(description(v2)).setFooter("Volume changed from " + (int) v + " to " + (int) v2, null).build()).queue(s -> Okita.musicCore.getMusicManager(context.guild).player.setVolume((int) v2));
                break;
        }
    }

    private String description(float v) {
        StringBuilder volume = new StringBuilder("[");

        for (int i = 0; i < (int) (v / 100.00 * 20.00); i++)
            volume.append("▬");

        volume.append("]()");

        for (int j = (int) (v / 100.00 * 20.00); j < 20; j++)
            volume.append("▬");

        volume.append(" **" + (int) v + "%**");

        return volume.toString();
    }
}