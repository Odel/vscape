package vscapeClient;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

import javax.sound.midi.*;

public final class MidiPlayer implements Receiver
{
	private static MidiPlayer instance = null;
	
	public MidiPlayer() throws Exception
	{
		
		resetChannels();
		receiver = MidiSystem.getReceiver();
		sequencer = MidiSystem.getSequencer(false);
		sequencer.getTransmitter().setReceiver(this);
		sequencer.open();
		setTick(-1L);
	}

    public static MidiPlayer getSingleton() {
    	try {
    		return instance = (instance == null ? new MidiPlayer() : instance);
		} catch (Exception e) {
			return null;
		}
    }
    
	public synchronized void setVolume(int velocity, int volume)
	{
		setVolume(velocity, volume, -1L);
	}

	public void play(final byte[] data, boolean loop, int volume)
	{
		try
		{
			sequencer.setSequence(MidiSystem.getSequence(new BufferedInputStream(new ByteArrayInputStream(data))));
			sequencer.setLoopCount(loop ? -1:0);
			setVolume(0, volume, -1L);
			sequencer.start();
		}
		catch (Exception ex) { }
	}

	public void stop()
	{
		sequencer.stop();
		setTick(-1L);
	}

	public void resetVolume(int volume)
	{
		resetVolume(volume, -1L);
	}

	private void setTick(long tick)
	{
		for (int i = 0; i != 16; ++i)
			sendMessage(i + 176, 123, 0, tick);

		for (int i = 0; i != 16; ++i)
			sendMessage(i + 176, 120, 0, tick);

		for (int i = 0; i != 16; ++i)
			sendMessage(i + 176, 121, 0, tick);

		for (int i = 0; i != 16; ++i)
			sendMessage(i + 176, 0, 0, tick);

		for (int i = 0; i != 16; ++i)
			sendMessage(i + 176, 32, 0, tick);

		for (int i = 0; i != 16; ++i)
			sendMessage(i + 192, 0, 0, tick);

	}

	private void sendMessage(int status, int data1, int data2, long tick)
	{
		try
		{
			ShortMessage msg = new ShortMessage();
			msg.setMessage(status, data1, data2);
			receiver.send(msg, tick);
		}
		catch (InvalidMidiDataException ex) { }
	}

	public void closeImpl()
	{
		sequencer.close();
		receiver.close();
	}

	private void resetVolume(int volume, long tick)
	{
		this.volume = volume;
		resetChannels();
		setVolume(tick);
	}

	private void setVolume(long tick)
	{
		for (int i = 0; i != 16; ++i)
		{
			int volume = getVolume(i);
			sendMessage(i + 176, 7, volume >>> 7, tick);
			sendMessage(i + 176, 39, volume & 0x7f, tick);
		}

	}

	private void setVolume(int velocity, int volume, long tick)
	{
		volume = (int) ((double) volume * Math.pow(0.1D, (double) velocity * 0.0005D) + 0.5D);
		if (this.volume == volume)
			return;

		this.volume = volume;
		setVolume(tick);
	}

	private int getVolume(int channel)
	{
		channel = channels[channel];
		return (int) (Math.sqrt((double) (channel = ((channel * volume) >>> 8) * channel)) + 0.5D);
	}

	private void resetChannels()
	{
		for (int i = 0; i != 16; ++i)
			channels[i] = 12800;

	}

	private boolean check(int status, int data1, int data2, long tick)
	{
		if ((status & 0xf0) == 176)
		{
			if (data1 == 121)
			{
				sendMessage(status, data1, data2, tick);
				int channel = status & 0xf;
				channels[channel] = 12800;
				int volume = getVolume(channel);
				sendMessage(status, 7, volume >>> 7, tick);
				sendMessage(status, 39, volume & 0x7f, tick);
				return true;
			}
			if (data1 == 7 || data1 == 39)
			{
				int channel = status & 0xf;
				if (data1 == 7)
					channels[channel] = (channels[channel] & 0x7f) | (data2 << 7);
				else
					channels[channel] = (channels[channel] & 0x3f80) | data2;

				int volume = getVolume(channel);
				sendMessage(status, 7, volume >>> 7, tick);
				sendMessage(status, 39, volume & 0x7f, tick);
				return true;
			}
		}
		return false;
	}

	public synchronized void send(MidiMessage msg, long tick)
	{
		byte[] data = msg.getMessage();
		if (data.length < 3 || !check(data[0], data[1], data[2], tick))
			receiver.send(msg, tick);

	}

	public void close()
	{
	}

	protected void finalize() throws Throwable
	{
		try
		{
			closeImpl();
		}
		finally
		{
			super.finalize();
		}
	}

	private int volume;
	private final int[] channels = new int[16];
	private final Receiver receiver;
	private final Sequencer sequencer;
}