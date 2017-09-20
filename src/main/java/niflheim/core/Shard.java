package niflheim.core;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import niflheim.Okita;
import niflheim.audio.GuildMusicManager;
import niflheim.listeners.EventListener;
import niflheim.listeners.ShardListener;
import niflheim.utils.GCounter;
import niflheim.utils.Settings;

public class Shard {
    private final int id;
    private EventListener eventListener;
    private ShardListener shardListener;
    private JDA jda;

    public Shard(int id, EventListener eventListener) {
        this.id = id;
        this.eventListener = eventListener;

        shardListener = new ShardListener();
        Okita.LOG.info("Constructing Shard " + id);

        jda = buildJda();
    }

    private JDA buildJda() {
        JDA jda = null;

        try {
            boolean start = false;

            while (!start) {
                JDABuilder builder = new JDABuilder(AccountType.BOT)
                        .setToken(Settings.TOKEN)
                        .addEventListener(eventListener, shardListener, Okita.waiter)
                        .setAudioSendFactory(new NativeAudioSendFactory())
                        .setAutoReconnect(true);

                if (Settings.SHARDS > 1)
                    builder.useSharding(id, Settings.SHARDS);

                try {
                    jda = builder.buildBlocking();
                    start = true;
                } catch (RateLimitedException e) {
                    Okita.LOG.warn("Rate limited while constructing Shards! Retrying...");
                }
            }
        } catch (Exception e) {
            Okita.LOG.error("Error while constructing Shard " + id + ": ", e);
        }

        return jda;
    }

    public int getId() {
        return id;
    }

    public ShardListener getShardListener() {
        return shardListener;
    }

    public EventListener getEventListener() {
        return eventListener;
    }

    public JDA getJda() {
        return jda;
    }

    public void revive() {
        for (String x: Okita.musicCore.getMusicManagers().keySet())
            Okita.musicCore.destroy(Okita.getGuildByID(x));

        jda.removeEventListener(eventListener, shardListener);
        jda.shutdown();

        jda = buildJda();
    }
}
