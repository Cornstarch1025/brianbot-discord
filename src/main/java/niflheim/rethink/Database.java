package niflheim.rethink;

import com.rethinkdb.gen.exc.ReqlDriverError;
import com.rethinkdb.net.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;

import static com.rethinkdb.RethinkDB.r;

public class Database {
    private static final Logger LOG = LoggerFactory.getLogger(Database.class);
    private final Connection conn;
    private final String name, gt, ut;

    public Database(String name, String gt, String ut) {
        Connection conn = null;
        this.name = name;
        this.gt = gt;
        this.ut = ut;

        try {
            conn = r.connection().hostname("localhost").port(28015).connect();

            if (r.dbList().<List<String>>run(conn).contains(name)) {
                LOG.info("Database connection established.");
                conn.use(name);
            } else {
                LOG.info("Rethink Database " + name + " not present, creating...");
                r.dbCreate(name).runNoReply(conn);
            }
        } catch (ReqlDriverError e) {
            LOG.error("Rethink Database connection failed.", e);
            System.exit(0);
        }

        if (!(Boolean) r.db(name).tableList().contains(gt).run(conn)) {
            LOG.info("Table " + gt + " does not exist... creating...");
            r.db(name).tableCreate("Guilds").runNoReply(conn);
        }

        if (!(Boolean) r.db(name).tableList().contains(ut).run(conn)) {
            LOG.info("Table " + ut + " does not exist... creating...");
            r.db(name).tableCreate("Users").runNoReply(conn);
        }

        this.conn = conn;
    }

    public boolean isOpen() {
        return conn != null && conn.isOpen();
    }

    public void close() {
        conn.close();
    }

    @Nullable
    public GuildOptions getGuildOptions(String id) {
        return isOpen() ? r.table(gt).get(id).run(conn, GuildOptions.class) : null;
    }

    @Nullable
    public UserOptions getUserOptions(String id) {
        return isOpen() ? r.table(ut).get(id).run(conn, UserOptions.class) : null;
    }

    public void saveGuildOptions(GuildOptions guildOptions) {
        if (isOpen())
            r.table(gt).insert(guildOptions)
                    .optArg("conflict", "replace")
                    .runNoReply(conn);
    }

    public void deleteGuildOptions(String id) {
        if (isOpen())
            r.table(gt).get(id)
                    .delete()
                    .runNoReply(conn);
    }

    public void saveUserOptions(UserOptions userOptions) {
        if (isOpen())
            r.table(ut).insert(userOptions)
                    .optArg("conflict", "replace")
                    .runNoReply(conn);
    }

    public void deleteUserOptions(String id) {
        if (isOpen())
            r.table(ut).get(id)
                    .delete()
                    .runNoReply(conn);
    }
}
