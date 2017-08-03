package niflheim.rethink;

import niflheim.Okita;

public class DatabaseRegistry {
    public GuildOptions ofGuild(String id) {
        GuildOptions options = Okita.DATABASE.getGuildOptions(id);

        return options != null ? options : new GuildOptions(id);
    }

    public UserOptions ofUser(String id) {
        UserOptions options = Okita.DATABASE.getUserOptions(id);
        return options != null ? options : new UserOptions(id);
    }

    public void deleteGuild(String id) {
        Okita.DATABASE.deleteGuildOptions(id);
    }

    public void deleteUser(String id) {
        Okita.DATABASE.deleteUserOptions(id);
    }
}
