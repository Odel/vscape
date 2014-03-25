import java.io.*;
import javax.sound.midi.*;
import javax.swing.*;

public class MidiPlayer {

	private int id;
	static Sequencer lastSeq;

	public static void playSong(int songId) {
		try {
			if (lastSeq != null)
				lastSeq.stop();
			Sequence sequence = MidiSystem.getSequence(new File("./cache/midi/"
					+ songId + ".mid"));
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.setSequence(sequence);
			lastSeq = sequencer;
			sequencer.setLoopCount(9001); // IT'S OVER
											// NINE-THOUUUUSAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAND!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			sequencer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void stopSong() {
		lastSeq.stop();
	}
}