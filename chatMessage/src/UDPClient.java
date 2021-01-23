//----IMPORT LIBRARY----//
import java.net.*;
import java.util.*;
//----UDP CLIENT----//
public class UDPClient {

    public static void main(String args[]) throws Exception {
        Scanner defaultInput = new Scanner(System.in);
        String host = defaultInput.nextLine();
        DatagramSocket socket = new DatagramSocket();
        messageReceiver r = new messageReceiver(socket);
        messageSender s = new messageSender(socket, host);
        Thread receiverThread = new Thread(r);
        Thread senderThread = new Thread(s);
        receiverThread.start();
        senderThread.start();
        }
    }
//----MESSAGE SENDER----//
class messageSender implements Runnable {
    public final static int PORT = 8080;
    private DatagramSocket sock;
    private String hostname;
    messageSender(DatagramSocket s, String h) {
        sock = s;
        hostname = h;
    }
    //----SENDING MESSAGE----//
    private void sendingMessage(String s) throws Exception {
        byte buf[] = s.getBytes();
        InetAddress address = InetAddress.getByName(hostname);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        sock.send(packet);
    }
    //----CONNECTION WITH SERVER----//
    public void run() {
        String message = "start";
        Scanner defaultInput = new Scanner(System.in);
        try {
            sendingMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        do {
            try {
                message = defaultInput.nextLine();
                if(!message.equalsIgnoreCase("end"))
                    sendingMessage(message);
            } catch(Exception e) {
                System.err.println(e);
            }
        } while (!message.equalsIgnoreCase("end"));
        try {
            Thread.sleep(100);
        } catch(Exception e) {
            System.err.println(e);
        }
        System.exit(0);
    }
}
//----MESSAGE RECEIVER----//
class messageReceiver implements Runnable {
    DatagramSocket sock;
    byte buf[];
    //----RECEIVING MESSAGE----//
    messageReceiver(DatagramSocket s) {
        sock = s;
        buf = new byte[1024];
    }
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                sock.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                if(!received.contains(" : start")) {
                    System.out.println(received);
                }
            } catch(Exception e) {
                System.err.println(e);
            }
        }
    }
}
