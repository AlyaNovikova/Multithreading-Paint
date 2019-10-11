import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by novikovaas.18 on 15.02.2017.
 */
public class Server implements MessageListener{
    static int port = 2391;
    static ArrayList<StreamWorker> postmans = new ArrayList<>();
    private boolean isDisconnected;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }

    public Server() {
        this.isDisconnected = false;
    }

    void onMessage(String text) {
        System.out.println(text);
        for (int i = 0; i < postmans.size(); i++) {
            StreamWorker postman = postmans.get(i);
            postman.sendMessage(text);
        }
    }

    @Override
    public void onDrag(int x1, int y1, int x2, int y2, int h, int col) {
        String text = "Drag\n" + x1 + "\n" + y1 + "\n" + x2 + "\n" + y2 + "\n" + h + "\n" + col;
        onMessage(text);
    }

    @Override
    public void onDisconnect() {
        this.isDisconnected = true;
    }

    public void run () throws IOException {
        ServerSocket server = new ServerSocket(port);

        while (true) {
            Socket client = server.accept();

            StreamWorker postman = new StreamWorker(client.getInputStream(), client.getOutputStream(), this);
            postmans.add(postman);
            postman.start();
        }
    }
}