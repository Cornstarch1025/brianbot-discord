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

        while (conn == null) {
            try {
                conn = r.connection().hostname("192.168.1.71").port(28015).connect();

                if (r.dbList().<List<String>>run(conn).contains(name)) {
                    LOG.info("Database connection established.");
                    conn.use(name);
                } else {
                    LOG.info("Rethink Database " + name + " not present, creating...");
                    r.dbCreate(name).runNoReply(conn);
                }
            } catch (ReqlDriverError e) {
                LOG.error("Rethink Database connection failed. Retrying...", e);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    LOG.error("Interrupted during connection cooldown!");
                }
            }
        }

        if (!(Boolean) r.db(name).tableList().contains(gt).run(conn)) {
            LOG.info("Table " + gt + " does not exist... creating...");
            r.db(name).tableCreate(gt).runNoReply(conn);
        }

        if (!(Boolean) r.db(name).tableList().contains(ut).run(conn)) {
            LOG.info("Table " + ut + " does not exist... creating...");
            r.db(name).tableCreate(ut).runNoReply(conn);
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
        return isOpen() ? r.db(name).table(gt).get(id).run(conn, GuildOptions.class) : null;
    }

    @Nullable
    public UserOptions getUserOptions(String id) {
        return isOpen() ? r.db(name).table(ut).get(id).run(conn, UserOptions.class) : null;
    }

    public void saveGuildOptions(GuildOptions guildOptions) {
        if (isOpen())
            r.db(name).table(gt).insert(guildOptions)
                    .optArg("conflict", "replace")
                    .runNoReply(conn);
    }

    public void deleteGuildOptions(String id) {
        if (isOpen())
            r.db(name).table(gt).get(id)
                    .delete()
                    .runNoReply(conn);
    }

    public void saveUserOptions(UserOptions userOptions) {
        if (isOpen())
            r.db(name).table(ut).insert(userOptions)
                    .optArg("conflict", "replace")
                    .runNoReply(conn);
    }

    public void deleteUserOptions(String id) {
        if (isOpen())
            r.db(name).table(ut).get(id)
                    .delete()
                    .runNoReply(conn);
    }
}
