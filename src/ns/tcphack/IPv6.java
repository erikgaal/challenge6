package ns.tcphack;

/**
 * Created by cotix on 3/19/15.
 */
public class IPv6 {
    public static byte[] myIP = new byte[]
            {       (byte) 0x20, (byte) 0x1, (byte) 0x6, (byte) 0x10,
                    (byte) 0x19, (byte) 0x8, (byte) 0xf0, (byte) 0,
                    (byte) 0x2, (byte) 0x1f, (byte) 0x1f, (byte) 0xff,
                    (byte) 0xfe, (byte) 0xbe, (byte) 0x28, (byte) 0x5d};

    public static byte[] remoteIP = new byte[]
            {       (byte) 0x20, (byte) 0x1, (byte) 0x6, (byte) 0x7c,
                    (byte) 0x25, (byte) 0x64, (byte) 0xa1, (byte) 70,
                    (byte) 0xa, (byte) 0x0, (byte) 0x27, (byte) 0xff,
                    (byte) 0xfe, (byte) 0x11, (byte) 0xce, (byte) 0xcb};

    public static byte[] makeHeaders(int payloadLen, byte[] srcAddress, byte[] dstAddress) {
        byte[] headers = new byte[40];
        headers[0] = 96;
        headers[4] = (byte)((payloadLen & 0xFF00) >> 8);
        headers[5] = (byte)(payloadLen & 0xFF);
        headers[6] = (byte)MyTcpHandler.VERSION;
        headers[7] = 64;
        for (int i = 0; i != 16; ++i) {
            headers[8 + i ] = srcAddress[i];
            headers[24 + i] = dstAddress[i];
        }
        return headers;
    }

    public static byte[] getSourceAddress(byte[] headers) {
        byte[] address = new byte[16];
        for (int i = 0; i != 16; ++i) {
            address[i] = headers[8 + i ];
        }
        return address;
    }

    public static byte[] getDestinationAddress(byte[] headers) {
        byte[] address = new byte[16];
        for (int i = 0; i != 16; ++i) {
            address[i] = headers[24 + i ];
        }
        return address;
    }

    public static int getPayloadLength(byte[] headers) {
        return ((headers[4] & 0xff) << 8) + (headers[5] & 0XFF);
    }
}
