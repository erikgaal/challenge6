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
        return ((packet[0] & 0xFF) << 8) + (packet[1] & 0xFF);
    }

    public int getDestinationPort() {
        return ((packet[2] & 0xFF) << 8) + (packet[3] & 0xFF);
    }

    public int getSequenceNumber() {
        return ((packet[4] & 0xFF) << 24) + ((packet[5] & 0xFF) << 16) + ((packet[6] & 0xFF) << 8) + (packet[7] & 0xFF);
    }

    public int getAcknowledgementNumber() {
        return ((packet[8] & 0xFF) << 24) + ((packet[9] & 0xFF) << 16) + ((packet[10] & 0xFF) << 8) + (packet[11] & 0xFF);
    }

    public int getDataOffset() {
        return (packet[12] & 0xFF) >> 4;
    }

    public int getControlBits() {
        return packet[13] & 63;
    }

    public int getWindow() {
        return ((packet[14] & 0xFF) << 8) + (packet[15] & 0xFF);
    }

    public int getChecksum() {
        return ((packet[16] & 0xFF) << 8) + (packet[17] & 0xFF);
    }

    public int getUrgentPointer() {
        return (packet[18] << 8) + (packet[19] & 0xFF);
    }

    public byte[] getData() {
        int offset = getDataOffset();
        byte[] data = new byte[packet.length - getDataOffset() * 4];
        System.arraycopy(packet, getDataOffset() * 4, data, 0, packet.length - getDataOffset() * 4);
        return data;
    }

    public byte[] getPacket() {
        return packet;
    }

    public TCPPacket(byte[] data) {
        packet = data;
    }

    public TCPPacket(int sourcePort, int destinationPort, int sequenceNumber, int acknowledgementNumber, int controlBits, int window, byte[] data, byte[] headers) {
        packet = new byte[5 * 4 + data.length]; // TODO: Options

        packet[0] = (byte) ((sourcePort & 0xFF00) >> 8);
        packet[1] = (byte) (sourcePort & 0xFF);
        packet[2] = (byte) ((destinationPort & 0xFF00) >> 8);
        packet[3] = (byte) (destinationPort & 0xFF);

        packet[4] = (byte) ((sequenceNumber & 0xFF000000) >> 24);
        packet[5] = (byte) ((sequenceNumber & 0xFF0000) >> 16);
        packet[6] = (byte) ((sequenceNumber & 0xFF00) >> 8);
        packet[7] = (byte) (sequenceNumber & 0xFF);

        packet[8] = (byte) ((acknowledgementNumber & 0xFF000000) >> 24);
        packet[9] = (byte) ((acknowledgementNumber & 0xFF0000) >> 16);
        packet[10] = (byte) ((acknowledgementNumber & 0xFF00) >> 8);
        packet[11] = (byte) (acknowledgementNumber & 0xFF);

        packet[12] = 6 << 2;
        packet[13] = (byte) (controlBits & 0x63);
        packet[14] = (byte) ((window & 0xFF00) >> 8);
        packet[15] = (byte) (window & 0xFF);

        packet[16] = 0;
        packet[17] = 0;
        packet[18] = 0;
        packet[19] = 0;

        System.arraycopy(data, 0, packet, 20, data.length);
        System.arraycopy(checksum(headers), 0, packet, 16, 2);
    }

    public byte[] checksum(byte[] ipheader) {
        byte[] temp = new byte[ipheader.length + packet.length];
        System.arraycopy(ipheader, 0, temp, 0, ipheader.length);
        System.arraycopy(packet, ipheader.length, temp, 0, packet.length);

        short result = 0;
        for (int i = 0; i < temp.length / 2; i++) {
            short data;
            if (i >= temp.length) {
                data = (short) ((temp[i] & 0xFF) << 8);
            } else {
                data = (short) (((temp[i] & 0xFF) << 8) + (temp[i+1] & 0xFF));
            }
            result += data;
        }
        return new byte[]{(byte) ((result & 0xFF00) >> 8), (byte) (result & 0xFF)};
    }
}
