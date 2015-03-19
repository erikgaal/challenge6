package ns.tcphack;

/**
 * Created by cotix on 3/19/15.
 */
public class IPv6 {
    public static byte[] makeHeaders(int payloadLen, byte[] srcAddress, byte[] dstAddress) {
        byte[] headers = new byte[40];
        headers[0] = 96;
        headers[4] = (byte)((payloadLen & 0xFF00) >> 8);
        headers[5] = (byte)(payloadLen & 0xFF);
        headers[6] = (byte)253;
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
        return (headers[4] << 8) + headers[5];
    }
}
