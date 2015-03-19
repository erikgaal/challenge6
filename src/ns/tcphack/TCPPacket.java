package ns.tcphack;

public class TCPPacket {
    public enum ControlBit {
        URG(32), ACK(16), PSH(8), RST(4), SYN(2), FIN(1);

        private final int value;

        ControlBit(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    byte[] packet;

    public int getSourcePort() {
        return ((packet[0] & 0xFF) << 8) + packet[1] & 0xFF;
    }

    public int getDestinationPort() {
        return ((packet[2] & 0xFF) << 8) + packet[3] & 0xFF;
    }

    public int getSequenceNumber() {
        return ((packet[4] & 0xFF) << 24) + ((packet[5] & 0xFF) << 16) + ((packet[6] & 0xFF) << 8) + packet[7] & 0xFF;
    }

    public int getAcknowledgementNumber() {
        return ((packet[8] & 0xFF) << 24) + ((packet[9] & 0xFF) << 16) + ((packet[10] & 0xFF) << 8) + packet[11] & 0xFF;
    }

    public int getDataOffset() {
        return (packet[12] & 0xFF) >> 4;
    }

    public int getControlBits() {
        return packet[13] & 63;
    }

    public int getWindow() {
        return ((packet[14] & 0xFF) << 8) + packet[15] & 0xFF;
    }

    public int getChecksum() {
        return ((packet[16] & 0xFF) << 8) + packet[17] & 0xFF;
    }

    public int getUrgentPointer() {
        return (packet[18] << 8) + packet[19] & 0xFF;
    }

    public byte[] getData() {
        int offset = getDataOffset();
        byte[] data = new byte[packet.length - getDataOffset() * 4];
        System.arraycopy(packet, getDataOffset() * 4, data, 0, packet.length);
        return data;
    }

    public TCPPacket(int sourcePort, int destinationPort, int sequenceNumber, int acknowledgementNumber, int controlBits, int window, byte[] data) {
        packet = new byte[5 * 4 + data.length]; // TODO: Options
        packet[0] = (byte) (sourcePort >> 8);
        packet[1] = (byte) (sourcePort);

        packet[2] = (byte) (destinationPort >> 8);
        packet[3] = (byte) destinationPort;

        packet[4] = (byte) (sequenceNumber >> 24);
        packet[5] = (byte) (sequenceNumber >> 16);
        packet[6] = (byte) (sequenceNumber >> 8);
        packet[7] = (byte) sequenceNumber;

        packet[8] = (byte) (acknowledgementNumber >> 24);
        packet[9] = (byte) (acknowledgementNumber >> 16);
        packet[10] = (byte) (acknowledgementNumber >> 8);
        packet[11] = (byte) acknowledgementNumber;

        packet[12] = 6 << 2;
    }
}
