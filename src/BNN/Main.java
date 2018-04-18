package BNN;

import BNN.network.Network;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    ContainerPanel containerPanel;
    Network network;
    Main() {
        containerPanel = new ContainerPanel();
        add(containerPanel);

        setTitle("Backpropagation neural network");
        setPreferredSize(new Dimension(600, 550));
        setMinimumSize(new Dimension(600, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        revalidate();
        setVisible(true);
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.network = new Network();
        DrawingPanel drawingPanel = new DrawingPanel();
        Options options = new Options();
        m.network.setOptionsListener(options);
        options.setDrawingPanelListener(drawingPanel);
        options.setNetworkListener(m.network);
        m.containerPanel.setDrawingPanel(drawingPanel);
        m.containerPanel.setOptions(options);
        m.containerPanel.setLayout();
        m.containerPanel.revalidate();
    }
}
