import java.io.*;
import java.net.SocketException;
import java.util.Objects;

public class StreamWorker implements Runnable, Closeable {

    private final BufferedReader in;
    private final PrintWriter out;
    private final MessageListener listener;

    private final Object outputLock = new Object();
    private final Object listenerLock = new Object();

    public StreamWorker(InputStream input, OutputStream output, MessageListener listener) {
        this.listener = listener;
        this.in = new BufferedReader(new InputStreamReader(input));
        this.out = new PrintWriter(output, true);
    }

    @Override
    public void run() {
        try {
            String s;

            while ((s = in.readLine()) != null) {

                synchronized (listenerLock) {
                    if (s.equals("Drag")) {
                        int x1 = Integer.parseInt(in.readLine());
                        int y1 = Integer.parseInt(in.readLine());
                        int x2 = Integer.parseInt(in.readLine());
                        int y2 = Integer.parseInt(in.readLine());
                        int h = Integer.parseInt(in.readLine());
                        int col = Integer.parseInt(in.readLine());
                        listener.onDrag(x1, y1, x2, y2, h, col);
                    }
                }
            }
        } catch (SocketException e) {
            if (e.getMessage().equals("Connection reset")) {
                synchronized (listenerLock) {
                    this.listener.onDisconnect();
                }
            } else {
                synchronized (listenerLock) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            synchronized (listenerLock) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        Thread thread = new Thread(this, "StreamWorker");
        thread.start();
    }

    public void sendMessage(String text) {
        synchronized (outputLock) {
            out.println(text);
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
    }
}