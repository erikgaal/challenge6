package ns.tcphack;

import java.io.UnsupportedEncodingException;

/**
 * Created by cotix on 3/19/15.
 */
public class TransportLayer {
    private NetworkLayer network;
    private byte[] dst;
    private int srcPort;
    private int dstPort;
    private int ackNumber;
    boolean connected;
    private int seqNumber;
    public TransportLayer(NetworkLayer netLayer, byte[] target, int port) {
        network = netLayer;
        dst = target;
        srcPort = (int)(Math.random()*Short.MAX_VALUE);
        dstPort = port;
        connected = false;
        seqNumber = 0;
        ackNumber = 0;
    }

    public boolean connected() {
        return connected;
    }

    public void close() {
        TCPPacket packet = new TCPPacket(srcPort, dstPort, seqNumber, 0,
                TCPPacket.ControlBit.FIN.getValue(), 64, new byte[0]);
        network.send(dst, packet.getPacket());
    }

    public void send(String message) {
        byte[] data = null;
        try {
            data = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        TCPPacket packet = new TCPPacket(srcPort, dstPort, seqNumber, 0, TCPPacket.ControlBit.PSH.getValue(), 0xFFFF, data);
        seqNumber += data.length;
        network.send(dst, packet.getPacket());
    }

    public String recv() {
        byte[] data = network.recv();
        if (data.length == 0) {
            return null;
        }
        TCPPacket packet = new TCPPacket(data);
        int len = packet.getData().length;
        if (packet.getData().length == 0) {
            len++;
        }
        ackNumber = packet.getSequenceNumber() + len;
        sendAck();
        if (packet.getData().length == 0) {
            if ((packet.getControlBits() & TCPPacket.ControlBit.SYN.getValue()) != 0) {
                System.out.println("CONNECTED!");
                connected = true;
            }
            return "";
        }
        try {
            return new String(packet.getData(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "Error";
    }

    public void sendAck() {
        byte[] data = new byte[0];
        TCPPacket packet = new TCPPacket(srcPort, dstPort, ++seqNumber, ackNumber,
                16, 0xFFFF, data);
        network.send(dst, packet.getPacket());
    }

    public void connect() {
        //Lets send a SYN packet to connect.
        byte[] data = new byte[0];
        TCPPacket packet = new TCPPacket(srcPort, dstPort, seqNumber, 0, TCPPacket.ControlBit.SYN.getValue(), 0xFFFF, data);
        network.send(dst, packet.getPacket());
    }
}
