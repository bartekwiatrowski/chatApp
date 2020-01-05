package client;

import server.ClientInfo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;



public class Client {

    private DatagramSocket socket;
    private InetAddress address;
    private int port;

    private boolean running;

    public Client(String name, String address, int port) {
        try {
            this.address = InetAddress.getByName(address);
            this.port = port;

            socket = new DatagramSocket();
            running = true;
            listen();
            send("\\con:" + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        try {

            message += "\\e";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            System.out.println("Sent Message To, " + address.getHostAddress() + ":" + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        Thread listenThread = new Thread("ChatProgram Listener") {
            public void run() {
                try {
                    while (running) {

                        byte[] data = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        socket.receive(packet);

                        String message = new String(data);
                        message = message.substring(0, message.indexOf("\\e"));


                        if (!isCommand(message, packet)) {
                            ClientWindow.printToConsole(message);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }; listenThread.start();
    }

    public static boolean isCommand(String message, DatagramPacket packet){

        if (message.startsWith("\\con: ")){

        }
        return false;
    }
}
