package niflheim.core;

import net.dv8tion.jda.core.JDA;
import niflheim.Okita;
import niflheim.listeners.ShardListener;
import niflheim.utils.Settings;

import java.util.List;

public class ShardMonitor extends Thread {
    private final int threshold = getThreshold();
    private boolean shutdown = false;

    @Override
    public void run() {
        Okita.LOG.info("Shard monitor thread started.");

        while (!shutdown) {
            try {
                inspect();
                sleep(10000);
            } catch (Exception e) {
                Okita.LOG.error("Exception while killing dead shards: ", e);
                try {
                    sleep(3000);
                } catch (InterruptedException ex) {
                    Okita.LOG.error("Interrupted while resetting Shard monitor!");
                }
            }
        }
    }

    private void inspect() throws InterruptedException {
        List<Shard> shards = Okita.shards;

        for (Shard shard : shards) {
            ShardListener listener = shard.getShardListener();

            long time = System.currentTimeMillis() - listener.getLastTime();

            if (time > threshold) {
                if (shard.getJda().getStatus() == JDA.Status.SHUTDOWN)
                    Okita.LOG.warn("Shard " + shard.getId() + " was not revived because it was shutdown!");
                else if (listener.getCount() < 100)
                    Okita.LOG.warn("Did not revive Shard " + shard.getId() + " because it didn't receive enough events since construction.");
                else {
                    Okita.LOG.info("Reviving Shard " + shard.getId() + " after " + (time / 1000) + " seconds of no events. Last event was " + listener.getLastTime());
                    shard.revive();
                    sleep(5000);
                }
            }
        }
    }

    private int getThreshold() {
        return Settings.SHARDS != 1 ? 60000 : 600000;
    }

    public void shutdown() {
        shutdown = true;
    }
}
