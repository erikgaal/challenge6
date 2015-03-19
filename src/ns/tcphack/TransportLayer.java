package ns.tcphack;

/**
 * Created by cotix on 3/19/15.
 */
public class TransportLayer {
    private NetworkLayer network;
    private Byte[] dst;
    private int srcPort;
    private int dstPort;
    public TransportLayer(NetworkLayer netLayer, Byte[] target, int port) {
        network = netLayer;
        dst = target;
        srcPort = (int)(Math.random()*Short.MAX_VALUE);
        dstPort = port;
    }

    public void connect() {
        //Lets send a SYN packet to connect.
        byte[] data = new byte[0];
        TCPPacket packet = new TCPPacket(srcPort, dstPort, 0, 0, TCPPacket.ControlBit.SYN.getValue(), 64, data);

    }
}
