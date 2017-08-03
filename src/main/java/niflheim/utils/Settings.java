package niflheim.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.List;

public class Settings {
    public static final Config CONFIG = ConfigFactory.parseFile(new File("resources/config.conf")).withFallback(ConfigFactory.load("resources/config.conf"));

    public static final String NAME = CONFIG.getString("bot.name");
    public static final String TOKEN = CONFIG.getString("bot.token");
    public static final String LOGS = CONFIG.getString("bot.logs");
    public static final String PM_LOG = CONFIG.getString("bot.pmlog");
    public static final int SHARDS = CONFIG.getInt("bot.shards");

    public static final String PREFIX = CONFIG.getString("admin.prefix");
    public static final List<String> ADMIN = CONFIG.getStringList("admin.admins");

    public static final String DBNAME = CONFIG.getString("database.name");
    public static final String GUILDS = CONFIG.getString("database.gtable");
    public static final String USERS = CONFIG.getString("database.utable");

    public static final String BOTSPW = CONFIG.getString("tokens.botspw");
    public static final String DBOTS = CONFIG.getString("tokens.dbots");
}
