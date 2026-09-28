package dkai.jade;

import javax.swing.*;
import java.awt.*;

public class TableView extends JPanel {

    public enum State { THINKING, WAITING, EATING }

    private static TableView view;

    private State[] states;
    private boolean[] forkFree;

    public static void open(int count) {
        view = new TableView();
        view.states = new State[count];
        view.forkFree = new boolean[count];
        for (int i = 0; i < count; i++) {
            view.states[i] = State.THINKING;
            view.forkFree[i] = true;
        }

        JFrame frame = new JFrame("Dining Philosophers");
        frame.add(view);
        frame.setSize(520, 540);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void setState(int id, State state) {
        view.states[id] = state;
        view.repaint();
    }

    public static void setFork(int id, boolean free) {
        view.forkFree[id] = free;
        view.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int n = states.length;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = 180;

        g.setColor(new Color(235, 225, 210));
        g.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        for (int i = 0; i < n; i++) {
            double forkAngle = 2 * Math.PI * (i + 0.5) / n;
            int forkX = centerX + (int) (radius * 0.7 * Math.cos(forkAngle));
            int forkY = centerY + (int) (radius * 0.7 * Math.sin(forkAngle));
            g.setColor(forkFree[i] ? Color.GRAY : Color.RED);
            g.fillRect(forkX - 4, forkY - 4, 8, 8);

            double angle = 2 * Math.PI * i / n;
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));

            if (states[i] == State.EATING) {
                g.setColor(Color.RED);
            } else if (states[i] == State.WAITING) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.GREEN);
            }
            g.fillOval(x - 30, y - 30, 60, 60);
            g.setColor(Color.BLACK);
            g.drawString("P" + i, x - 8, y + 5);
        }

        g.drawString("Green: thinking, Yellow: waiting, Red: eating, Red square: fork in use", 10, 20);
    }
}