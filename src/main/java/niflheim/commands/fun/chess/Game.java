package niflheim.commands.fun.chess;

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

@CommandFrame(
        aliases = {"challenge"},
        help = "Challenges Stockfish Chess AI to a game.",
        usage = ".game <white/black> <0-10 difficulty>",
        cooldown = 3000L,
        category = Category.FUN,
        scope = Scope.TEXT
)
public class Game extends Command {
    public void execute(Context context, String[] args) {
        UserOptions options = Okita.registry.ofUser(context.user.getId());

        switch (args.length) {
            default:
                context.invalid(this);
                break;
            case 0:
                context.channel.sendMessage("Please choose a side!").queue();
                break;
            case 1:
                if (args[0].equalsIgnoreCase("white"))
                    init(0, 1, context);
                else if (args[0].equalsIgnoreCase("black"))
                    init(1, 1, context);
                else
                    context.channel.sendMessage("Please choose a valid side!").queue();
                break;
            case 2:
                int side = 0;
                int difficulty = 1;

                if (args[0].equalsIgnoreCase("white"))
                    side = 0;
                else if (args[0].equalsIgnoreCase("black"))
                    side = 1;
                else {
                    context.channel.sendMessage("Please choose a valid side!").queue();
                    return;
                }

                if (options.getLevel() < 1)
                    context.channel.sendMessage("Sorry! Chess computation is very resource taxing, setting custom difficulty is restricted to Level 1 Donators. You have initialized a game with a difficulty of 1.").queue();
                else {
                    try {
                        difficulty = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        context.channel.sendMessage("Please choose a valid difficulty!").queue();
                        return;
                    }

                    if (difficulty < 0 || difficulty > 10) {
                        context.channel.sendMessage("Please choose a difficulty between 0 and 10").queue();
                        return;
                    }

                    init(side, difficulty, context);
                }
        }
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