package niflheim.rethink;

import javax.annotation.Nullable;
import java.beans.ConstructorProperties;

public class UserOptions {
    private final String id;
    private String fen;
    private String playlist;

    private int level = 0;

    @ConstructorProperties("id")
    public UserOptions(String id) {
        this.id = id;
    }

    @Nullable
    public String getFen() {
        return fen;
    }

    @Nullable
    public String getPlaylist() {
        return playlist;
    }

    public int getLevel() {
        return level;
    }

    public void setFEN(String fen) {
        this.fen = fen;
    }

    public void setPlaylist(String playlist) {
        this.playlist = playlist;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
