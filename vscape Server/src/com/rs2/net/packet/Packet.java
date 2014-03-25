package com.rs2.net.packet;

import com.rs2.net.StreamBuffer;

public class Packet {

	private int opcode;
	private int packetLength;
	private StreamBuffer.InBuffer in;

	public Packet(int opcode, int packetLength, StreamBuffer.InBuffer in) {
		this.opcode = opcode;
		this.packetLength = packetLength;
		this.in = in;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public int getOpcode() {
		return opcode;
	}

	public void setPacketLength(int packetLength) {
		this.packetLength = packetLength;
	}

	public int getPacketLength() {
		return packetLength;
	}

	public void setIn(StreamBuffer.InBuffer in) {
		this.in = in;
	}

	public StreamBuffer.InBuffer getIn() {
		return in;
	}

}