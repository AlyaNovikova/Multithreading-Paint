import sun.applet.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client implements MessageListener {

    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.1", 2391);
        client.run();
    }
    private MainFrame world;
    private final String host;
    private final int port;
    private boolean isDisconnected;
    private StreamWorker postman;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.isDisconnected = false;
    }

    public void onDrag(int x1, int y1, int x2, int y2, int h, int col) {
        postman.sendMessage("Drag\n" + x1 + "\n" + y1 + "\n" + x2 + "\n" + y2 + "\n" + h + "\n" + col);
    }

    public void onDisconnect() {
        this.isDisconnected = true;
    }

    public void run() throws IOException {
        Socket socket = new Socket(this.host, this.port);

        MainFrame mainFrame = new MainFrame(this);

        postman = new StreamWorker(socket.getInputStream(), socket.getOutputStream(), mainFrame);
        postman.start();
    }
}