package BNN;

import BNN.listeners.DrawingPanelListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener, DrawingPanelListener {
    int[] points;

    DrawingPanel() {
        setPreferredSize(new Dimension(350, 500));
        addMouseListener(this);
        addMouseMotionListener(this);
        setVisible(true);

        points = new int[70];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.gray);
        for(int i = 0; i < 7; i ++) {
            g2d.drawLine(i * 50, 0, i * 50, getHeight());
        }
        g2d.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
        for(int i = 0; i < 10; i++) {
            g2d.drawLine(0, i * 50, getWidth(), i * 50);
        }
        g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

        g2d.setColor(Color.black);
        for(int i = 0; i < points.length; i++) {
            if(points[i] == 1)
                g2d.fillRect(i % 7 * 50, i / 7 * 50, 50, 50);
        }
    }

    private void setPointDragging(MouseEvent e) {
        if (e.getX() < getWidth() && e.getY() < getHeight()) {
            int mx = e.getX();
            int my = e.getY();
            int index = my / 50 * 7 + mx / 50;
            points[index] = 1;
        }
        repaint();
    }

    private void setPoint(MouseEvent e) {
        if (e.getX() < getWidth() && e.getY() < getHeight()) {
            int mx = e.getX();
            int my = e.getY();
            int index = my / 50 * 7 + mx / 50;
            if (points[index] == 0)
                points[index] = 1;
            else
                points[index] = 0;
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setPoint(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        setPointDragging(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }


    @Override
    public void mouseMoved(MouseEvent e) {
    }


    @Override
    public void clearDrawingPane() {
        for(int i = 0; i < points.length; i++) {
            points[i] = 0;
        }
        repaint();
    }

    @Override
    public int[] getImagePane() {
        return points;
    }
}
