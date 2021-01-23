//----IMPORT LIBRARY----//
import java.io.*;
import java.net.*;
import java.util.*;
//----UDP SERVER----//
public class UDPServer extends Thread {
    public final static int PORT = 8080;
    private final static int BUFFER = 1024;
    //----INITIALIZATION OF VARIABLES, LISTS, ETC.----//
    private DatagramSocket socket;
    private ArrayList<InetAddress> clientAddresses;
    private ArrayList<Integer> clientPorts;
    private HashSet<String> existingClients;
    public UDPServer() throws IOException {
        socket = new DatagramSocket(PORT);
        clientAddresses = new ArrayList();
        clientPorts = new ArrayList();
        existingClients = new HashSet();
    }

    public void run() {
        byte[] buf = new byte[BUFFER];
        while (true) {
            try {
                Arrays.fill(buf, (byte)0);
                DatagramPacket message = new DatagramPacket(buf, buf.length);
                socket.receive(message);

                String content = new String(buf,0, message.getLength());
                InetAddress clientAddress = message.getAddress();
                int clientPort = message.getPort();

                String id = clientAddress.toString() + "," + clientPort;
                if (!existingClients.contains(id)) {
                    existingClients.add(id);
                    clientPorts.add(clientPort);
                    clientAddresses.add(clientAddress);
                }

                System.out.println(id + " : " + content);
                byte[] data = (id + " : " + content).getBytes();
                for (int i = 0; i < clientAddresses.size(); i++) {
                    InetAddress client = clientAddresses.get(i);
                    int portClient = clientPorts.get(i);
                    message = new DatagramPacket(data, data.length, client, portClient);
                    socket.send(message);
                }
            } catch(Exception e) {
                System.err.println(e);
            }
        }
    }
    //----MAIN----//
    public static void main(String args[]) throws Exception {
        UDPServer s = new UDPServer();
        s.start();
    }
}