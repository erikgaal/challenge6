package ns.tcphack;

import java.util.Arrays;

/**
 * Created by cotix on 3/19/15.
 */
public class IPv6Test {

    public static void printPacket(byte[] packet) {
        for (int i = 0; i != packet.length; ++i) {
            System.out.print(packet[i] + " ");
        }
        System.out.println();
    }

    public static int rand() {
        return (int)(Math.random()*Integer.MAX_VALUE);
    }

    public static boolean test() {
        int payloadLen = rand() & 0xFFFF;
        byte[] src = new byte[16];
        byte[] dst = new byte[16];
        for (int i = 0; i != 16; ++i) {
            src[i] = (byte)((rand()) & 0xFF);
            dst[i] = (byte)((rand()) & 0xFF);
        }
        boolean res = true;
        byte[] headers = IPv6.makeHeaders(payloadLen, src, dst);
        if (!Arrays.equals(IPv6.getDestinationAddress(headers),dst)) {
            System.out.println("getDst not working");
            res = false;
        }
        if (!Arrays.equals(IPv6.getSourceAddress(headers),src)) {
            System.out.println("getSrc not working");
            res = false;
        }
        if (IPv6.getPayloadLength(headers) != payloadLen) {
            System.out.println("getPayloadLen not working");
            res = false;
        }
        return res;
    }
    public static void main(String[] args) {

        for (int i = 0; i != 1000000; ++i) {
            if (!test()) {
                System.out.println("IPv6 class is broken! " + i);
                return;
            }
        }
        System.out.println("IPv6 testing returned succesfully!");
    }
}
