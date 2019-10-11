import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.BUTTON2;
import static java.awt.event.MouseEvent.BUTTON3;
import static java.awt.event.MouseWheelEvent.WHEEL_UNIT_SCROLL;

/**
 * Created by alya on 07.02.2017.
 */
public class MainFrame extends JFrame implements MouseListener, MouseMotionListener, MouseWheelListener, MessageListener {

    ArrayList<Integer> xs1 = new ArrayList<>();
    ArrayList<Integer> xs2 = new ArrayList<>();
    ArrayList<Integer> h_s = new ArrayList<>();
    ArrayList<Integer> ys1 = new ArrayList<>();
    ArrayList<Integer> ys2 = new ArrayList<>();
    ArrayList<Integer> c = new ArrayList<>();
    MessageListener listener;
    int h, cur;
    int cur_x, cur_y;
    int x, y;
    int col;

    public MainFrame(MessageListener listener) {
        this.h = 10;
        this.cur = 0;
        this.x = 0;
        this.y = 0;
        this.col = 239;
        setPreferredSize(new Dimension(800, 600));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        pack();
        createBufferStrategy(2);

        this.listener = listener;
    }

    @Override
    public void paint(Graphics g) {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(2);
            bufferStrategy = getBufferStrategy();
        }
        g = bufferStrategy.getDrawGraphics();
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) g;


        for (int i = 0; i < xs1.size(); i++) {
            g.setColor(new Color(c.get(i)));
            g2d.setStroke(new BasicStroke(h_s.get(i), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawLine(xs1.get(i), ys1.get(i), xs2.get(i), ys2.get(i));
        }

        g.setColor(new Color(col));
        g2d.setStroke(new BasicStroke(h / 2));
        g.fillOval(x - h / 2, y - h / 2, h, h);
        g.dispose();
        bufferStrategy.show();
    }

    boolean flag = false;

    @Override
    public void mouseClicked(MouseEvent e) {
        repaint();
    }

    Random random = new Random();

    @Override
    public void mousePressed(MouseEvent e) {
        flag = true;
        cur_x = e.getX();
        cur_y = e.getY();
        if (e.getButton() == BUTTON3) {
            col = random.nextInt();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        flag = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (flag) {
            listener.onDrag(e.getX(), e.getY(), cur_x, cur_y, h, col);
            cur_x = e.getX();
            cur_y = e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        cur += e.getUnitsToScroll();
        cur = Math.max(cur, -20);
        h = Math.max(cur / 2 + 10, 0);
        repaint();
    }

    public void onDrag(int x1, int y1, int x2, int y2, int h, int color) {
        xs1.add(x1);
        xs2.add(x2);
        c.add(color);
        col = color;
        h_s.add(h);
        ys1.add(y1);
        ys2.add(y2);
        repaint();
    }

    public void onDisconnect() {

    }
}