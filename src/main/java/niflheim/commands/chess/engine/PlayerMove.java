package niflheim.commands.chess.engine;

import net.dv8tion.jda.core.entities.User;

public class PlayerMove {
    private final User user;
    private final String move;
    private final String fen;

    public PlayerMove(User user, String move, String fen) {
        this.user = user;
        this.move = move;
        this.fen = fen;
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

    public String getId() {
        return user.getId();
    }
}
