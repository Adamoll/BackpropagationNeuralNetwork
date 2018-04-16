package BNN;

import javax.swing.*;
import java.awt.*;

public class ContainerPanel extends JPanel {
    DrawingPanel drawingPanel;
    Options options;

    public void setLayout() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 10);
        add(drawingPanel, gbc);
        gbc.gridx ++;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(options, gbc);
        setPreferredSize(new Dimension(drawingPanel.getWidth() + options.getWidth(), drawingPanel.getHeight() + options.getHeight()));

        drawingPanel.repaint();
        options.repaint();
        repaint();
        setVisible(true);
    }

    public DrawingPanel getDrawingPanel() {
        return drawingPanel;
    }

    public void setDrawingPanel(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }
}
