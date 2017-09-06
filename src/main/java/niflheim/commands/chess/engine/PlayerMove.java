package niflheim.commands.chess.engine;

import net.dv8tion.jda.core.entities.User;
import niflheim.core.Context;

public class PlayerMove {
    private final User user;
    private final String move;
    private final String fen;
    private final int commandType;
    public final Context ctx;

    public PlayerMove(User user, String move, String fen, int commandType, Context ctx) {
        this.user = user;
        this.move = move;
        this.fen = fen;
        this.commandType = commandType;
        this.ctx = ctx;
    }

    public User getUser() {
        return user;
    }

    public String getMove() {
        return move;
    }

    public String getFen() {
        return fen;
    }

    public int getCommandType() {
        return commandType;
    }

    public String getId() {
        return user.getId();
    }
}
