package niflheim;

import com.jagrosh.jdautilities.waiter.EventWaiter;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import niflheim.audio.MusicCore;
import niflheim.commands.LoadCommands;
import niflheim.commands.chess.engine.Stockfish;
import niflheim.commands.chess.engine.StockfishQueue;
import niflheim.core.Shard;
import niflheim.core.ShardMonitor;
import niflheim.listeners.EventListener;
import niflheim.rethink.Database;
import niflheim.rethink.DatabaseRegistry;
import niflheim.utils.GCounter;
import niflheim.utils.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Okita {
    public static final Logger LOG = LoggerFactory.getLogger(Okita.class);
    public static Database DATABASE;
    public static DatabaseRegistry registry = new DatabaseRegistry();
    public static EventWaiter waiter = new EventWaiter();
    public static MusicCore musicCore = new MusicCore();
    public static ArrayList<Shard> shards = new ArrayList<>();
    public static Stockfish stockfish = new Stockfish();
    public static StockfishQueue stockfishQueue = new StockfishQueue();
    public static ShardMonitor monitor;
    public static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
        System.out.println("   ___    _      _   _           \n" +
                "  / _ \\  | | __ (_) | |_    __ _ \n" +
                " | | | | | |/ / | | | __|  / _` |\n" +
                " | |_| | |   <  | | | |_  | (_| |\n" +
                "  \\___/  |_|\\_\\ |_|  \\__|  \\__,_|\n" +
                "                                 ");

        DATABASE = new Database(Settings.DBNAME, Settings.GUILDS, Settings.USERS);

        if (stockfish.startEngine()) {
            LOG.info("Stockfish Engine successfully started.");
            stockfish.sendCommand("uci");
            stockfish.sendCommand("setoption name Threads value " + Runtime.getRuntime().availableProcessors() / 2);
            stockfish.getOutput(0, 0);
        } else
            LOG.info("Something went wrong starting Stockfish.");

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
                Thread.sleep(5000);
            } catch (Exception e) {
                LOG.error("Error starting Shard " + i + ": ", e);
            }
        }

        executor.scheduleAtFixedRate(() -> {
            GCounter.guilds = 0;
            for (Shard shard : shards) {
                GCounter.update(shard);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 30, TimeUnit.MINUTES);
    }
}
