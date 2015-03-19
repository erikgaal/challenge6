package ns.tcphack;

class MyTcpHandler extends TcpHandler {
	public static void main(String[] args) {
		new MyTcpHandler();
	}

	public MyTcpHandler() {
		super();

		boolean done = false;

        NetworkLayer nLayer = new NetworkLayer(this, IPv6.myIP);
        TransportLayer tLayer = new TransportLayer(nLayer, IPv6.remoteIP, 7711);
        tLayer.connect();
		while (!done) {
            String result = tLayer.recv();
            if (result != null) {
                System.out.println("Packet: " + result);
            }
		}   
	}
}
