package niflheim.commands.chess;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;
import niflheim.rethink.UserOptions;

import java.awt.*;

@CommandFrame(
        name = "Resign",
        example = ".resign",
        aliases = {"quit"},
        help = "Resigns your current Chess game.",
        usage = ".resign",
        cooldown = 3000L,
        category = Category.CHESS,
        scope = Scope.TEXT
)
public class Resign extends Command {
    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        UserOptions options = Okita.registry.ofUser(context.user.getId());

        if (options.getFen() == null) {
            context.channel.sendMessage("Player does not currently have a game!").queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN)
                .setAuthor(context.user.getName() + "'s Game", null, context.user.getEffectiveAvatarUrl())
                .setThumbnail(context.user.getEffectiveAvatarUrl())
                .setDescription("Player has resigned the game. Computer wins!")
                .setFooter("Chess powered by Stockfish 8", null);

        options.setFEN(null);
        options.save();

        context.channel.sendMessage(embed.build()).queue();
    }
}