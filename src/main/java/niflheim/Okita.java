package niflheim;

import niflheim.rethink.Database;
import niflheim.rethink.DatabaseRegistry;
import niflheim.utils.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Okita {
    public static final Logger LOG = LoggerFactory.getLogger(Okita.class);
    public static final Database DATABASE = new Database(Settings.DBNAME, Settings.GUILDS, Settings.USERS);

    private static final DatabaseRegistry registry = new DatabaseRegistry();

    public static void main(String[] args) {

    }

    public static DatabaseRegistry getRegistry() {
        return registry;
    }
}
