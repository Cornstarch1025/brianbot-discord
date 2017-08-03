package niflheim;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import niflheim.audio.MusicCore;
import niflheim.commands.LoadCommands;
import niflheim.core.Shard;
import niflheim.core.ShardMonitor;
import niflheim.listeners.EventListener;
import niflheim.listeners.EventWaiter;
import niflheim.rethink.Database;
import niflheim.rethink.DatabaseRegistry;
import niflheim.utils.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Okita {
    public static final Logger LOG = LoggerFactory.getLogger(Okita.class);
    public static Database DATABASE = new Database(Settings.DBNAME, Settings.GUILDS, Settings.USERS);
    public static DatabaseRegistry registry = new DatabaseRegistry();
    public static EventWaiter waiter = new EventWaiter();
    public static MusicCore musicCore = new MusicCore();
    public static ArrayList<Shard> shards = new ArrayList<>();
    public static ShardMonitor monitor;

    public static void main(String[] args) {
        System.out.println("   ___    _      _   _           \n" +
                "  / _ \\  | | __ (_) | |_    __ _ \n" +
                " | | | | | |/ / | | | __|  / _` |\n" +
                " | |_| | |   <  | | | |_  | (_| |\n" +
                "  \\___/  |_|\\_\\ |_|  \\__|  \\__,_|\n" +
                "                                 ");

        init(new EventListener());
        LoadCommands.init();

        monitor = new ShardMonitor();
        monitor.setDaemon(true);
        monitor.start();
    }

    public static List<Guild> getAllGuilds() {
        ArrayList<Guild> guilds = new ArrayList<>();

        for (Shard shard : shards)
            guilds.addAll(shard.getJda().getGuilds());

        return guilds;
    }

    public static Map<String, User> getAllUsersAsMap() {
        HashMap<String, User> users = new HashMap<>();

        for (Shard shard : shards)
            for (User user : shard.getJda().getUsers())
                users.put(user.getId(), user);

        return users;
    }

    public static Guild getGuildByID(String id) {
        for (Guild guild : getAllGuilds())
            if (guild.getId().equalsIgnoreCase(id))
                return guild;

        return null;
    }

    public static TextChannel getTextChannelById(String id) {
        for (Shard shard : shards)
            for (TextChannel channel : shard.getJda().getTextChannels())
                if (channel.getId().equalsIgnoreCase(id))
                    return channel;

        return null;
    }

    public static VoiceChannel getVoiceChannelByID(String id) {
        for (Shard shard : shards)
            for (VoiceChannel channel : shard.getJda().getVoiceChannels())
                if (channel.getId().equalsIgnoreCase(id))
                    return channel;

        return null;
    }

    private static void init(EventListener listener) {
        for (int i = 0; i < Settings.SHARDS; i++) {
            try {
                shards.add(i, new Shard(i, listener));
            } catch (Exception e) {
                LOG.error("Error starting Shard " + i + ": ", e);
            }
        }
    }
}
