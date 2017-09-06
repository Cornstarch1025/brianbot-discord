package niflheim.commands.chess.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Stockfish {

	private Process engineProcess;
	private BufferedReader processReader;
	private OutputStreamWriter processWriter;

	private static final String PATH = "resources/stockfish";
	//private static final String PATH = "resources/stockfish.exe";

	public boolean startEngine() {
		try {
			engineProcess = Runtime.getRuntime().exec(PATH);
			processReader = new BufferedReader(new InputStreamReader(
					engineProcess.getInputStream()));
			processWriter = new OutputStreamWriter(
					engineProcess.getOutputStream());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void sendCommand(String command) {
		try {
			processWriter.write(command + "\n");
			processWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getOutput(int waitTime) {
		StringBuffer buffer = new StringBuffer();
		try {
			Thread.sleep(waitTime);
			sendCommand("isready");
			while (true) {
				String text = processReader.readLine();
				if (text.equals("readyok"))
					break;
				else
					buffer.append(text + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public String movePiece(String fen, String move) {
	    sendCommand("position fen " + fen + " moves " + move);
	    sendCommand("d");
	    return getOutput(0);
    }

	public String getBestMove(String fen, int waitTime) {
		sendCommand("position fen " + fen);
		sendCommand("go movetime " + waitTime);
		return getOutput(waitTime + 20).split("bestmove ")[1].split(" ")[0];
	}

	public void stopEngine() {
		try {
			sendCommand("quit");
			processReader.close();
			processWriter.close();
		} catch (IOException e) {
		}
	}

	public String getLegalMoves(String fen) {
		sendCommand("position fen " + fen);
		sendCommand("perft 1");
		return getOutput(0);
	}

	public void drawBoard(String fen) {
		sendCommand("position fen " + fen);
		sendCommand("d");

		String[] rows = getOutput(0).split("\n");

		for (int i = 1; i < 18; i++) {
			System.out.println(rows[i]);
		}
	}

	public float getEvalScore(String fen, int waitTime) {
		sendCommand("position fen " + fen);
		sendCommand("go movetime " + waitTime);

		float evalScore = 0.0f;
		String[] dump = getOutput(waitTime + 20).split("\n");
		for (int i = dump.length - 1; i >= 0; i--) {
			if (dump[i].startsWith("info depth ")) {
				try {
				evalScore = Float.parseFloat(dump[i].split("score cp ")[1]
						.split(" nodes")[0]);
				} catch(Exception e) {
					evalScore = Float.parseFloat(dump[i].split("score cp ")[1]
							.split(" upperbound nodes")[0]);
				}
			}
		}
		return evalScore/100;
	}
}
