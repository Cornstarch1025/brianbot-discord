package niflheim.commands.chess;

import com.jagrosh.jdautilities.menu.buttonmenu.ButtonMenuBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.commands.chess.engine.PlayerMove;
import niflheim.core.Context;
import niflheim.rethink.UserOptions;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@CommandFrame(
        name = "Game",
        example = ".game",
        aliases = {"challenge"},
        help = "Challenges Stockfish Chess AI to a game.",
        usage = ".game",
        cooldown = 3000L,
        category = Category.CHESS,
        scope = Scope.TEXT
)
public class Game extends Command {
    private ConcurrentHashMap<User, StringBuilder> gameSetter = new ConcurrentHashMap<>();

    private final String WHITE = "\u2B1C";
    private final String BLACK = "\u2B1B";
    private final String CANCEL = "\u274C";

    private final String ONE = "\u0031\u20E3";
    private final String TWO = "\u0032\u20E3";
    private final String THREE = "\u0033\u20E3";
    private final String FOUR = "\u0034\u20E3";
    private final String FIVE = "\u0035\u20E3";

    private MessageEmbed restrict = new EmbedBuilder().setColor(Color.CYAN)
            .setDescription("Chess Engine computation is very resource intensive. Please consider donating to unlock difficulty selection. Your game has been defaulted to level 1 difficulty.").build();

    private ButtonMenuBuilder side = new ButtonMenuBuilder()
            .setColor(Color.CYAN)
            .setDescription("Please select your starting side by choosing \u2B1C or \u2B1B! To cancel, press the \u274C!")
            .setEventWaiter(Okita.waiter)
            .setChoices(WHITE, BLACK, CANCEL)
            .setTimeout(1, TimeUnit.MINUTES);

    private ButtonMenuBuilder difficulty = new ButtonMenuBuilder()
            .setColor(Color.CYAN)
            .setDescription("Okita's chess is powered by Stockfish 8 Chess Engine, please select your computer difficulty below. You cannot change this setting in-game so choose wisely!")
            .setEventWaiter(Okita.waiter)
            .setChoices(ONE, TWO, THREE, FOUR, FIVE, CANCEL)
            .setTimeout(1, TimeUnit.MINUTES);

    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        UserOptions options = Okita.registry.ofUser(context.user.getId());

        if (options.getFen() != null) {
            context.channel.sendMessage("You already have an ongoing game! To start a new game, resign first!").queue();
            return;
        }

        side.setAction(e -> {
            gameSetter.put(context.user, new StringBuilder());
            switch (e.getName()) {
                case WHITE:
                    gameSetter.get(context.user).append("w");
                    break;
                case BLACK:
                    gameSetter.get(context.user).append("b");
                    break;
                case CANCEL:
                    gameSetter.remove(context.user);
                    return;
            }

            if (options.getLevel() > -1)
                difficulty.setAction(f -> {
                    switch (f.getName()) {
                        case ONE:
                            gameSetter.get(context.user).append(" 1");
                            break;
                        case TWO:
                            gameSetter.get(context.user).append(" 2");
                            break;
                        case THREE:
                            gameSetter.get(context.user).append(" 3");
                            break;
                        case FOUR:
                            gameSetter.get(context.user).append(" 4");
                            break;
                        case FIVE:
                            gameSetter.get(context.user).append(" 5");
                            break;
                        case CANCEL:
                            gameSetter.remove(context.user);
                            break;
                    }
                    init(context, gameSetter.get(context.user).toString().split("\\s+"));
                    gameSetter.remove(context.user);
                }).setUsers(context.user).build().display(context.channel);
            else {
                gameSetter.get(context.user).append(" 1");
                init(context, gameSetter.get(context.user).toString().split("\\s+"));
                context.channel.sendMessage(restrict).queue(s -> {
                    if (context.guild.getSelfMember().hasPermission(context.channel, Permission.MESSAGE_MANAGE))
                        s.delete().queueAfter(10, TimeUnit.SECONDS);
                });
            }
        }).setUsers(context.user).build().display(context.channel);
    }

    private void init(Context context, String[] args) {
        String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        UserOptions options = Okita.registry.ofUser(context.user.getId());

        options.setFEN(FEN);
        options.setDifficulty(Integer.parseInt(args[1]));
        options.save();

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN)
                .setAuthor(context.user.getName() + "'s Game", null, context.user.getEffectiveAvatarUrl())
                .setFooter("Chess powered by Stockfish 8", null);

        if (args[0].equalsIgnoreCase("w"))
            context.channel.sendMessage(embed.setDescription("Player started game as white.")
                    .setThumbnail("http://www.fen-to-image.com/image/24/single/coords/" + options.getFen().split("\\s+")[0])
                    .build()).queue();
        else {
            Okita.stockfishQueue.playerMove(new PlayerMove(context.user, null, options.getFen(), 2, context));
        }
    }
}