package vscapeClient;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public final class SoundProvider {

    private static SoundProvider instance = null;
	private final ExecutorService playerPool = Executors.newCachedThreadPool();
	private Sequencer midiSequencer;
	private Sequence midiSequence;
	private Clip clip;
	private Position curPosition;

	private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb

	enum Position {
		LEFT, RIGHT, NORMAL
	};
    protected SoundProvider() {
		try {
					this.midiSequencer = MidiSystem.getSequencer();
				} catch (MidiUnavailableException e) {
				
				}
			}
    
    /** @return the singleton instance */
    public static SoundProvider getSingleton() {
        return instance = (instance == null ? new SoundProvider() : instance);
    }
    
    /** plays a MIDI sequence 
    * @param data the midi sequence bytes to be read
    */
    public void playMIDI(final byte[] data, final int loopCount){
		if (!midiSequencer.isOpen())
			try {
				midiSequencer.open();
			} catch (MidiUnavailableException e1) {
			}
		if(midiSequencer.isRunning())midiSequencer.stop();

		playerPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					midiSequence = MidiSystem.getSequence(new BufferedInputStream(new ByteArrayInputStream(data)));
					midiSequencer.setLoopCount(loopCount);
					midiSequencer.setSequence(midiSequence);
				} catch (InvalidMidiDataException e) {
				} catch (IOException e) {
				}
				midiSequencer.start();
			}
		});
	}

    public void stopMidi()
	{
    	midiSequencer.stop();
    	midiSequencer.close();
	}
    
   /** plays a WAV
    * @param data the WAV bytes to be read
    */
    public void playWAV(final byte[] data) {
		new Thread() {
			@Override
			public void run() {
				;

				AudioInputStream audioInputStream = null;
				try {
					audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
				} catch (UnsupportedAudioFileException e1) {
					e1.printStackTrace();
					return;
				} catch (IOException e1) {
					e1.printStackTrace();
					return;
				}

				AudioFormat format = audioInputStream.getFormat();
				SourceDataLine auline = null;
				DataLine.Info info = new DataLine.Info(SourceDataLine.class,
						format);

				try {
					auline = (SourceDataLine) AudioSystem.getLine(info);
					auline.open(format);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
					return;
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

				if (auline.isControlSupported(FloatControl.Type.PAN)) {
					FloatControl pan = (FloatControl) auline
							.getControl(FloatControl.Type.PAN);
					if (curPosition == Position.RIGHT)
						pan.setValue(1.0f);
					else if (curPosition == Position.LEFT)
						pan.setValue(-1.0f);
				}

				auline.start();
				int nBytesRead = 0;
				byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];

				try {
					while (nBytesRead != -1) {
						nBytesRead = audioInputStream.read(abData, 0,
								abData.length);
						if (nBytesRead >= 0)
							auline.write(abData, 0, nBytesRead);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				} finally {
					auline.drain();
					auline.close();
				}

			}
		}.start();
	}

    public void playMP3(byte [] data) {
        
    }
}