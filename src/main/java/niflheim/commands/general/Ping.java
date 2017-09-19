package niflheim.commands.general;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.core.Context;

import java.awt.*;

@CommandFrame(
        name = "Ping",
        example = ".ping",
        aliases = {"pong"},
        help = "Checks Okita's ping as well as the websocket.",
        usage = ".ping",
        cooldown = 1000L
)
public class Ping extends Command {
    public void execute(Context context, String[] args) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setAuthor("Pong!", null, context.jda.getSelfUser().getAvatarUrl())
                .setDescription("Awaiting response...");

        long time = System.currentTimeMillis();

        context.channel.sendMessage(embed.build()).queue(s -> s.editMessage(embed.setDescription("Response in: **" + (System.currentTimeMillis() - time) + " ms**\nWebsocket: **" + context.jda.getPing() + " ms**").build()).queue());
    }
}
