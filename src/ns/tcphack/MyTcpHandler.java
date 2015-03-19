package ns.tcphack;

class MyTcpHandler extends TcpHandler {
    public static int VERSION = 253;

	public static void main(String[] args) {
		new MyTcpHandler();
	}

	public MyTcpHandler() {
		super();

		boolean done = false;
        boolean sent = false;
        NetworkLayer nLayer = new NetworkLayer(this, IPv6.myIP);
        TransportLayer tLayer = new TransportLayer(nLayer, IPv6.remoteIP, 7719);
        tLayer.connect();
		while (!done) {
            String result = tLayer.recv();
            if (result != null) {
                System.out.println("Packet: " + result);
            }
            if (tLayer.connected() && !sent) {
                sent = true;
                tLayer.send("GET / HTTP/1.0\n\n");
            }
		}   
	}
}
