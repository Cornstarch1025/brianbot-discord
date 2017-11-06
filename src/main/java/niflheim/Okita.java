package niflheim;

import com.jagrosh.jdautilities.waiter.EventWaiter;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import niflheim.audio.MusicCore;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.LoadCommands;
import niflheim.commands.chess.engine.Stockfish;
import niflheim.commands.chess.engine.StockfishQueue;
import niflheim.core.Core;
import niflheim.core.Shard;
import niflheim.core.ShardMonitor;
import niflheim.listeners.EventListener;
import niflheim.rethink.Database;
import niflheim.rethink.DatabaseRegistry;
import niflheim.utils.GCounter;
import niflheim.utils.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
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

    public static void main(String[] args) throws IOException{
        System.out.println("   ___    _      _   _           \n" +
                "  / _ \\  | | __ (_) | |_    __ _ \n" +
                " | | | | | |/ / | | | __|  / _` |\n" +
                " | |_| | |   <  | | | |_  | (_| |\n" +
                "  \\___/  |_|\\_\\ |_|  \\__|  \\__,_|\n" +
                "                                 ");
/*
        DATABASE = new Database(Settings.DBNAME, Settings.GUILDS, Settings.USERS);

        if (stockfish.startEngine()) {
            LOG.info("Stockfish Engine successfully started.");
            stockfish.sendCommand("uci");
            stockfish.sendCommand("setoption name Threads value " + Runtime.getRuntime().availableProcessors() / 2);
            stockfish.getOutput(0, 0);
        } else
            LOG.info("Something went wrong starting Stockfish.");
*/
        //init(new EventListener());
        LoadCommands.init();
/*
        monitor = new ShardMonitor();
        monitor.setDaemon(true);
        monitor.start();
*/
        write();
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
    }

    public static void write() throws IOException{
        PrintWriter writer = new PrintWriter("lol.txt", "UTF-8");

        writer.println("  <div class=\"table\">\n" +
                "    \n" +
                "    <div class=\"row header\">\n" +
                "      <div class=\"cell\">\n" +
                "        Utility Commands\n" +
                "      </div>\n" +
                "      <div class=\"cell\">\n" +
                "        Info\n" +
                "      </div>\n" +
                "      <div class=\"cell\">\n" +
                "        Usage\n" +
                "      </div>\n" +
                "      <div class=\"cell\">\n" +
                "        Example Usage\n" +
                "      </div>\n" +
                "    </div>");

        for (Command command: Core.getCommands().values()) {
            if (command.getInfo().category() == Category.UTILITY)
            writer.println("    <div class=\"row\">\n" +
                    "      <div class=\"cell\">\n" +
                    "        " + command.getInfo().name() + "\n" +
                    "      </div>\n" +
                    "      <div class=\"cell\">\n" +
                    "        " + command.getInfo().help() +"\n" +
                    "      </div>\n" +
                    "      <div class=\"cell\">\n" +
                    "        " + command.getInfo().usage() + "\n" +
                    "      </div>\n" +
                    "      <div class=\"cell\">\n" +
                    "        " + command.getInfo().example() + "\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "    ");
        }

        writer.println("</div>");
        writer.close();
    }
}
