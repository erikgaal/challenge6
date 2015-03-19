package ns.tcphack;

class MyTcpHandler extends TcpHandler {
	public static void main(String[] args) {
		new MyTcpHandler();
	}

	public MyTcpHandler() {
		super();

		boolean done = false;
        byte[] myAddress = new byte[]
                {       (byte) 0x20, (byte) 0x1, (byte) 0x6, (byte) 0x10,
                        (byte) 0x19, (byte) 0x8, (byte) 0xf0, (byte) 0,
                        (byte) 0x2, (byte) 0x1f, (byte) 0x1f, (byte) 0xff,
                        (byte) 0xfe, (byte) 0xbe, (byte) 0x28, (byte) 0x5d};

        byte[] remoteAddress = new byte[]
                {       (byte) 0x20, (byte) 0x1, (byte) 0x6, (byte) 0x7c,
                        (byte) 0x25, (byte) 0x64, (byte) 0xa1, (byte) 70,
                        (byte) 0xa, (byte) 0x0, (byte) 0x27, (byte) 0xff,
                        (byte) 0xfe, (byte) 0x11, (byte) 0xce, (byte) 0xcb};
        NetworkLayer nLayer = new NetworkLayer(this, myAddress);
        TransportLayer tLayer = new TransportLayer(nLayer, remoteAddress, 7711);
        tLayer.connect();
		while (!done) {
            String result = tLayer.recv();
            if (result != null) {
                System.out.println("Packet: " + result);
            }
		}   
	}
}
