package niflheim.commands.chess.engine;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class MoveQueue {
    private BlockingQueue<PlayerMove> queue =  new LinkedBlockingDeque<>();
}
