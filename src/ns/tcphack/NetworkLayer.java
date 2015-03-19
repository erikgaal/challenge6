package ns.tcphack;


/**
 * Created by cotix on 3/19/15.
 */
public class NetworkLayer {
    TcpHandler handler;
    byte[] src;
    public NetworkLayer(TcpHandler tcp, byte[] myAddress) {
        src = myAddress;
        handler = tcp;
    }

    public byte[] getOwnAddress() {
        return src;
    }

    public void send(byte[] dst, byte[] data) {
        byte[] headers = IPv6.makeHeaders(data.length, src, dst);
        byte[] packet = new byte[headers.length + data.length];
        System.arraycopy(headers, 0, packet, 0, headers.length);
        System.arraycopy(data, 0, packet, headers.length, data.length);
        handler.sendData(packet);
    }

    public byte[] recv() {
        byte[] data = handler.receiveData(1);
        if (data.length == 0) {
            return new byte[0];
        }
        byte[] res = new byte[data.length - 40];
        System.arraycopy(data, 40, res, 0, data.length - 40);
        return res;
    }
}
