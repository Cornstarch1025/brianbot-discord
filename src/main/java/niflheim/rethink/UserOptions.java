package niflheim.rethink;

import niflheim.Okita;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.HashMap;

public class UserOptions {
    private final String id;
    private String fen;
    private HashMap<String, ArrayList<String>> playlist;

    private int level = 0;

    @ConstructorProperties("id")
    public UserOptions(String id) {
        this.id = id;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nullable
    public String getFen() {
        return fen;
    }

    @Nullable
    public HashMap<String, ArrayList<String>> getPlaylist() {
        return playlist;
    }

    public int getLevel() {
        return level;
    }

    public void setFEN(String fen) {
        this.fen = fen;
    }

    public void addPlaylist(String name, ArrayList<String> playlist) {
        this.playlist.put(name, playlist);
    }

    public void removePlaylist(String name) {
        playlist.remove(name);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void save(){
        Okita.DATABASE.saveUserOptions(this);
    }

    public void delete(){
        Okita.DATABASE.deleteUserOptions(id);
    }
}
