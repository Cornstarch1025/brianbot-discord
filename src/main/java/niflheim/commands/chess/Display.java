package niflheim.commands.chess;

import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.commands.chess.engine.PlayerMove;
import niflheim.core.Context;
import niflheim.rethink.UserOptions;

@CommandFrame(
        help = "Displays User's current chess game.",
        usage = ".display",
        cooldown = 3000L,
        category = Category.CHESS,
        scope = Scope.TEXT
)
public class Display extends Command {
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

        Okita.stockfishQueue.playerMove(new PlayerMove(context.user, null, options.getFen(), 0, context));
    }
}