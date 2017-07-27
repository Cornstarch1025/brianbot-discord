package niflheim.listeners;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.EventListener;

public class ShardListener implements EventListener {
    private Event last = null;
    private long lastTime = System.currentTimeMillis();
    private int count = 0;

    @Override
    public void onEvent(Event event) {
        last = event;
        lastTime = System.currentTimeMillis();
        count++;
    }

    public Event getLast() {
        return last;
    }

    public long getLastTime() {
        return lastTime;
    }

    public int getCount() {
        return count;
    }
}
