package BNN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Options extends JPanel implements ActionListener, OptionsListener{
    private JLabel weightRangesLabel;
    private JLabel hiddenLayerSizeLabel;
    private JLabel learningRateLabel;
    private JLabel momentumLabel;
    private JLabel outputLabel;
    private JLabel errorLabel;
    private JLabel effectivenessLabel;

    private JTextField weighRangesField;
    private JTextField hiddenLayerSizeField;
    private JTextField learningRateField;
    private JTextField momentumRateField;

    private ButtonGroup momentumBG;
    private JRadioButton momentumOffRB;
    private JRadioButton momentumOnRB;

    private JButton startLearningButton;
    private JButton stopLearningButton;
    private JButton runButton;
    private JButton clearButton;
    private JButton loadParametersButton;
    private JButton runValidationSetButton;

    private DrawingPanelListener drawingPanelListener;
    private NetworkListener networkListener;

    Options() {
        startLearningButton = new JButton("Start learning");
        stopLearningButton = new JButton("Stop learning");
        runButton = new JButton("Run network");
        clearButton = new JButton("Clear panel");
        loadParametersButton = new JButton("Load params");
        runValidationSetButton = new JButton("Run val. set");

        startLearningButton.addActionListener(this);
        stopLearningButton.addActionListener(this);
        runButton.addActionListener(this);
        clearButton.addActionListener(this);
        loadParametersButton.addActionListener(this);
        runValidationSetButton.addActionListener(this);

        weightRangesLabel = new JLabel("W. ranges:");
        hiddenLayerSizeLabel = new JLabel("Hidd. layer size: ");
        learningRateLabel = new JLabel("Learn. rate:");
        momentumLabel = new JLabel("Momentum:");
        effectivenessLabel = new JLabel("Effectiv.: ");
        effectivenessLabel.setPreferredSize(new Dimension(100, 0));
        errorLabel = new JLabel("Error: ");
        outputLabel = new JLabel("Output: ");
        outputLabel.setFont(new Font("Dialog", Font.BOLD, 16));

        weighRangesField = new JTextField(4);
        weighRangesField.setText(NetworkUtils.minWeight + ";" + NetworkUtils.maxWeight);
        hiddenLayerSizeField = new JFormattedTextField(4);
        hiddenLayerSizeField.setText(NetworkUtils.hiddenLayerSize + "");
        learningRateField = new JTextField(4);
        learningRateField.setText(NetworkUtils.learningRate + "");
        momentumRateField = new JTextField(4);
        momentumRateField.setText(NetworkUtils.momentumRate + "");

        momentumOnRB = new JRadioButton("On");
        momentumOnRB.addActionListener(this);
        momentumOffRB = new JRadioButton("Off");
        momentumOffRB.addActionListener(this);
        momentumOffRB.setSelected(true);

        momentumBG = new ButtonGroup();
        momentumBG.add(momentumOffRB);
        momentumBG.add(momentumOnRB);

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gridBagLayout);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2,0,0 ,5);
        add(runButton, gbc);

        gbc.gridx ++;
        add(outputLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy ++;
        add(hiddenLayerSizeLabel, gbc);

        gbc.gridx ++;
        add(hiddenLayerSizeField, gbc);

        gbc.gridx = 0;
        gbc.gridy ++;
        add(weightRangesLabel, gbc);

        gbc.gridx ++;
        add(weighRangesField, gbc);

        gbc.gridx = 0;
        gbc.gridy ++;
        add(learningRateLabel, gbc);

        gbc.gridx ++;
        add(learningRateField, gbc);

        gbc.gridx = 0;
        gbc.gridy ++;
        add(momentumLabel, gbc);

        gbc.gridx ++;
        add(momentumRateField, gbc);

        gbc.gridx = 0;
        gbc.gridy ++;
        add(momentumOffRB, gbc);

        gbc.gridx ++;
        add(momentumOnRB, gbc);

        gbc.gridx = 0;
        gbc.gridy ++;
        gbc.insets = new Insets(0,0,0 ,5);
        add(startLearningButton, gbc);

        gbc.gridx ++;
        add(effectivenessLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy ++;
        add(stopLearningButton, gbc);

        gbc.gridx ++;
        add(errorLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy ++;
        add(runValidationSetButton, gbc);

        gbc.gridy ++;
        add(clearButton, gbc);

        gbc.gridy ++;
        add(loadParametersButton, gbc);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startLearningButton) {
            networkListener.startLearning();
        } else if (e.getSource() == stopLearningButton) {
            networkListener.stopLearning();
        } else if (e.getSource() == runButton) {
            outputLabel.setText("Output: " + networkListener.runNetwork(drawingPanelListener.getImagePane()));
        } else if (e.getSource() == clearButton) {
            drawingPanelListener.clearDrawingPane();
        } else if (e.getSource() == runValidationSetButton) {
            double[] result = networkListener.runOnValidationSet();
            errorLabel.setText("Error: " + String.format("%1$,.2f", result[0]));
            effectivenessLabel.setText("Effectiv.: " + String.format("%1$,.2f", result[1]));
        } else if (e.getSource() == loadParametersButton) {
            double[] ranges = getWeightRanges(weighRangesField.getText());
            NetworkUtils.minWeight = ranges[0];
            NetworkUtils.maxWeight = ranges[1];
            NetworkUtils.learningRate = Double.parseDouble(learningRateField.getText());
            NetworkUtils.hiddenLayerSize = Integer.parseInt(hiddenLayerSizeField.getText());
            NetworkUtils.momentum = momentumOnRB.isSelected();
            NetworkUtils.momentumRate = Double.parseDouble(momentumRateField.getText());

            networkListener.reinitialize();
        }
    }

    private double[] getWeightRanges(String ranges) {
        String[] rangesSplit = ranges.split(";");
        double min = Double.parseDouble(rangesSplit[0]);
        double max = Double.parseDouble(rangesSplit[1]);
        return new double[]{min, max};
    }

    @Override
    public void updateEffectivenessLabel(double effectiveness) {
        effectivenessLabel.setText("Effectiv.: " + String.format("%1$,.2f", effectiveness) + " %");
    }

    @Override
    public void updateErrorLabel(double error) {
        errorLabel.setText("Error: " + String.format("%1$,.2f", error));
    }

    public DrawingPanelListener getDrawingPanelListener() {
        return drawingPanelListener;
    }

    public void setDrawingPanelListener(DrawingPanelListener drawingPanelListener) {
        this.drawingPanelListener = drawingPanelListener;
    }

    public NetworkListener getNetworkListener() {
        return networkListener;
    }

    public void setNetworkListener(NetworkListener networkListener) {
        this.networkListener = networkListener;
    }
}
