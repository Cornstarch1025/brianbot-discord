package niflheim.commands.chess.engine;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.Okita;
import niflheim.rethink.UserOptions;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class StockfishQueue {
    private BlockingQueue<PlayerMove> queue = new LinkedBlockingDeque<>();

    public StockfishQueue() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    PlayerMove move = queue.poll(1000, TimeUnit.MILLISECONDS);

                    if (move != null) {
                        UserOptions options = Okita.registry.ofUser(move.getId());
                        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN)
                                .setAuthor(move.ctx.user.getName() + "'s Game", null, move.ctx.user.getEffectiveAvatarUrl())
                                .setFooter("Pawn promotion works by adding `q`ueen, `b`ishop, k`n`ight, or `r`ook to end of move.", null);

                        switch (move.getCommandType()) {
                            case 0:
                                //get Legal moves
                                String legal = Okita.stockfish.getLegalMoves(move.getFen()).replaceAll(": 1\n", ", ");

                                move.ctx.channel.sendMessage(embed.setDescription("Legal moves: `" + legal.substring(0, legal.length() - 2) + "`")
                                        .setThumbnail("http://www.fen-to-image.com/image/24/single/coords/" + move.getFen().split("\\s+")[0])
                                        .build()).queue();
                                break;
                            case 1:
                                EmbedBuilder embed1 = new EmbedBuilder().setColor(Color.CYAN)
                                        .setAuthor(move.ctx.user.getName() + "'s Game", null, move.ctx.user.getEffectiveAvatarUrl())
                                        .setFooter("Chess powered by Stockfish 8", null);

                                String[] legalMoves = Okita.stockfish.getLegalMoves(move.getFen()).split(": 1\n");

                                if (!Arrays.asList(legalMoves).contains(move.getMove().toLowerCase())) {
                                    move.ctx.channel.sendMessage("Illegal move!").queue();
                                    break;
                                }

                                options.setFEN(Okita.stockfish.movePiece(move.getFen(), move.getMove().toLowerCase()).split("Fen: ")[1].split("\n")[0]);
                                options.save();

                                if (!Okita.stockfish.getLegalMoves(options.getFen()).contains(":")) {
                                    move.ctx.channel.sendMessage(embed1.setDescription("Player moves " + move.getMove() + " for checkmate! Player wins!")
                                            .setThumbnail("http://www.fen-to-image.com/image/24/single/coords/" + options.getFen().split("\\s+")[0])
                                            .build()).queue();

                                    options.setFEN(null);
                                    options.save();
                                    break;
                                }

                                Okita.stockfish.sendCommand("setoption name Skill Level value " + options.getDifficulty() * 4);
                                String bestMove = Okita.stockfish.getBestMove(options.getFen(), 100);
                                options.setFEN(Okita.stockfish.movePiece(options.getFen(), bestMove).split("Fen: ")[1].split("\n")[0]);
                                options.save();

                                if (!Okita.stockfish.getLegalMoves(options.getFen()).contains(":")) {
                                    move.ctx.channel.sendMessage(embed1.setDescription("Computer moves " + bestMove + " for checkmate! Computer wins!")
                                            .setThumbnail("http://www.fen-to-image.com/image/24/single/coords/" + options.getFen().split("\\s+")[0])
                                            .build()).queue();

                                    options.setFEN(null);
                                    options.save();
                                    break;
                                }

                                move.ctx.channel.sendMessage(embed1.setDescription("Player moved " + move.getMove().toLowerCase() + ". Computer moved " + bestMove)
                                        .setThumbnail("http://www.fen-to-image.com/image/24/single/coords/" + options.getFen().split("\\s+")[0])
                                        .build()).queue();
                                break;
                            case 2:
                                EmbedBuilder embed2 = new EmbedBuilder().setColor(Color.CYAN)
                                        .setAuthor(move.ctx.user.getName() + "'s Game", null, move.ctx.user.getEffectiveAvatarUrl())
                                        .setFooter("Chess powered by Stockfish 8", null);

                                Okita.stockfish.sendCommand("setoption name Skill Level value " + options.getDifficulty());
                                String bestMove1 = Okita.stockfish.getBestMove(options.getFen(), 100);
                                options.setFEN(Okita.stockfish.movePiece(options.getFen(), bestMove1).split("Fen: ")[1].split("\nKey")[0]);
                                options.save();

                                move.ctx.channel.sendMessage(embed2.setDescription("Player has started game as black. Computer moved " + bestMove1)
                                        .setThumbnail("http://www.fen-to-image.com/image/24/single/coords/" + options.getFen().split("\\s+")[0])
                                        .build()).queue();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void playerMove(PlayerMove move) {
        queue.add(move);
    }
}
