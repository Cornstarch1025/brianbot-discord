package niflheim.commands.fun.chess;

import com.jagrosh.jdautilities.menu.buttonmenu.ButtonMenu;
import com.jagrosh.jdautilities.menu.buttonmenu.ButtonMenuBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;
import niflheim.core.Core;
import niflheim.rethink.UserOptions;
import niflheim.utils.Stockfish;

import java.awt.*;
import java.util.concurrent.TimeUnit;

@CommandFrame(
        aliases = {"challenge"},
        help = "Challenges Stockfish Chess AI to a game.",
        usage = ".game",
        cooldown = 3000L,
        category = Category.FUN,
        scope = Scope.TEXT
)
public class Game extends Command {
    private final String WHITE = "\u2B1C";
    private final String BLACK = "\u2B1B";
    private final String CANCEL = "\u274C";

    private final String ONE = "\u0031\u20E3";
    private final String TWO = "\u0032\u20E3";
    private final String THREE = "\u0033\u20E3";
    private final String FOUR = "\u0034\u20E3";
    private final String FIVE = "\u0035\u20E3";

    private ButtonMenuBuilder side = new ButtonMenuBuilder()
            .setColor(Color.CYAN)
            .setDescription("Please select your starting side by choosing \u2B1C or \u2B1B! To cancel, press the \u274C!")
            .setEventWaiter(Okita.waiter)
            .setChoices(WHITE, BLACK, CANCEL)
            .setTimeout(1, TimeUnit.MINUTES);

    private ButtonMenuBuilder difficulty = new ButtonMenuBuilder()
            .setColor(Color.CYAN)
            .setDescription("Please select a computer difficulty below! Okita's chess is powered by Stockfish 8, one of the strongest Chess Engines in the world. Once selected the difficulty can not be changed without starting a new game!")
            .setEventWaiter(Okita.waiter)
            .setChoices(ONE, TWO, THREE, FOUR, FIVE, CANCEL)
            .setTimeout(1, TimeUnit.MINUTES);

    public void execute(Context context, String[] args) {
        if (args.length != 0){
            context.invalid(this);
            return;
        }

        UserOptions options = Okita.registry.ofUser(context.user.getId());

        if (options.getFen() != null){
            context.channel.sendMessage("You already have an ongoing game! To start a new game, resign first!").queue();
            return;
        }

        side.setAction(e -> {
            switch (e.getName()){
                case WHITE:
                    break;
                case BLACK:
                    break;
                case CANCEL:
                    break;
            }

            difficulty.setAction(f -> {
                switch (f.getName()){
                    case ONE:
                        break;
                    case TWO:
                        break;
                    case THREE:
                        break;
                    case FOUR:
                        break;
                    case FIVE:
                        break;
                    case CANCEL:
                        break;
                }
            }).setUsers(context.user).build().display(context.channel);
        }).setUsers(context.user).build().display(context.channel);
    }

    private void init(int side, int difficulty, Context context) {
        String FEN = side == 0 ? "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" : "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1";
        UserOptions options = Okita.registry.ofUser(context.user.getId());

        if (options.getFen() != null){
            context.channel.sendMessage("User has existing game! `.resign` to end game or `.display` to check game state.").queue();
            return;
        }

        Stockfish stockfish = new Stockfish();

        if (stockfish.startEngine()){
            stockfish.sendCommand("setoption name Skill Level value " + difficulty*2);
            Core.getPlayers().put(context.user, stockfish);

            
        } else
            context.channel.sendMessage("Stockfish Chess Engine failed to start... please try again later.").queue();
    }
}