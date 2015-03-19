package ns.tcphack;

import java.util.Arrays;

public class TCPPacket {
    byte[] packet;

    public int getSourcePort() {
        return (packet[0] << 8) + packet[1];
    }

    public int getDestinationPort() {
        return (packet[2] << 8) + packet[3];
    }

    public int getSequenceNumber() {
        return (packet[4] << 24) + (packet[5] << 16) + (packet[6] << 8) + packet[7];
    }

    public int getAcknowledgementNumber() {
        return (packet[8] << 24) + (packet[9] << 16) + (packet[10] << 8) + packet[11];
    }

    public int getDataOffset() {
        return packet[12] >> 4;
    }

    public int getControlBits() {
        return packet[13] & 63;
    }

    public int getWindow() {
        return (packet[14] << 8) + packet[15];
    }

    public int getChecksum() {
        return (packet[16] << 8) + packet[17];
    }

    public int getUrgentPointer() {
        return (packet[18] << 8) + packet[19];
    }

    public byte[] getData() {
        int offset = getDataOffset();
        byte[] data = new byte[packet.length - getDataOffset() * 4];
        System.arraycopy(packet, getDataOffset() * 4, data, 0, packet.length);
        return data;
    }
}
