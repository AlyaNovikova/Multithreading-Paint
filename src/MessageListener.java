public interface MessageListener {

    void onDrag(int x1, int y1, int x2, int y2, int h, int col);

    void onDisconnect();
}