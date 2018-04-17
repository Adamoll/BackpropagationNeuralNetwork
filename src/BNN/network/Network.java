package BNN;

import java.util.ArrayList;

public class Network implements NetworkListener {
    private ImageReader readerLearningSet;
    private ImageReader readerValidationSet;
    private ArrayList<Neuron> inputLayer;
    private ArrayList<Neuron> hiddenLayer;
    private ArrayList<Neuron> outputLayer;
    private Neuron biasNeuron;
    private int validPredictions = 0;
    private double oldError;
    private int epoch = 0;
    private OptionsListener optionsListener;

    public Network() {
        initialize();
    }

    public void initialize() {
        inputLayer = new ArrayList<>();
        hiddenLayer = new ArrayList<>();
        outputLayer = new ArrayList<>();
        biasNeuron = new Neuron();
        oldError = 0;

        readerLearningSet = new ImageReader("res/learningSet/");
        readerValidationSet = new ImageReader("res/validationSet/");

        // initialize layers
        // input layer
        for(int i = 0; i < NetworkUtils.inputLayerSize; i++) {
            Neuron neuron = new Neuron();
            inputLayer.add(neuron);
        }

        // hidden layer
        for(int i = 0; i < NetworkUtils.hiddenLayerSize; i++) {
            Neuron neuron = new Neuron();
            neuron.addInConnections(inputLayer);
            neuron.addBiasConnection(biasNeuron);
            hiddenLayer.add(neuron);
        }

        // output layer
        for(int i = 0; i < NetworkUtils.outputLayerSize; i++) {
            Neuron neuron = new Neuron();
            neuron.addInConnections(hiddenLayer);
            neuron.addBiasConnection(biasNeuron);
            outputLayer.add(neuron);
        }

        // initialize connections weight
        // input to hidden layer connections
        for (Neuron neuron : hiddenLayer) {
            for (Connection connection : neuron.getInConnection()) {
                connection.setWeight(NetworkUtils.getRandomDouble());
            }
        }
        // hidden to output layer connections
        for (Neuron neuron : outputLayer) {
            for (Connection connection : neuron.getInConnection()) {
                connection.setWeight(NetworkUtils.getRandomDouble());
            }
        }

        setOutConnections(outputLayer);
    }

    public int propagateForward(LearningPattern pattern, boolean train) {
        // load pattern into input layer neurons
        for (int i = 0; i < NetworkUtils.inputLayerSize; i++) {
            inputLayer.get(i).setNet(pattern.getArray()[i]);
        }

        // calculate stimulation of hidden layer neurons
        for (int i = 0; i < NetworkUtils.hiddenLayerSize; i++) {
            Neuron hiddenNeuron = hiddenLayer.get(i);
            double netH = 0;
            for (int j = 0; j < NetworkUtils.inputLayerSize; j++) {
                netH += hiddenNeuron.getInConnection().get(j).getWeight() * hiddenNeuron.getInConnection().get(j).getLeft().getNet();
            }
            netH += hiddenNeuron.getBiasConnection().getWeight() * NetworkUtils.biasValue;
            hiddenNeuron.setNet(netH);
            hiddenNeuron.setfNet(NetworkUtils.costFunction(netH));
        }

        // calculate stimulation of output layer neurons
        for (int i = 0; i < NetworkUtils.outputLayerSize; i++) {
            Neuron outputNeuron = outputLayer.get(i);
            double netO = 0;
            for (int j = 0; j < NetworkUtils.hiddenLayerSize; j++) {
                netO += outputNeuron.getInConnection().get(j).getWeight() * outputNeuron.getInConnection().get(j).getLeft().getfNet();
            }
            netO += outputNeuron.getBiasConnection().getWeight() * NetworkUtils.biasValue;
            outputNeuron.setNet(netO);
            outputNeuron.setfNet(NetworkUtils.costFunction(netO));
        }

        // checking if most stimulated neuron from output layer is the same as expected label from pattern
        double max = Double.MIN_VALUE;
        int indBest = 0;
        for(int i = 0; i < outputLayer.size(); i++) {
            if (outputLayer.get(i).getfNet() > max) {
                max = outputLayer.get(i).getfNet();
                indBest = i;
            }
        }
        if(pattern.getExpectedClass() == indBest) {
            validPredictions++;
        }

        if(train)
            propagateBackward(pattern);
        return indBest;
    }


    private void propagateBackward(LearningPattern pattern) {
        // calculate output error and update connections weight between output and hidden layers
        for(int i = 0; i < NetworkUtils.outputLayerSize; i++) {
            double y = i == pattern.getExpectedClass() ? 1 : 0;
            double errorO = (y - outputLayer.get(i).getfNet()) *
                    NetworkUtils.costFunctionDerivative(outputLayer.get(i).getNet());
            outputLayer.get(i).setError(errorO);
            ArrayList<Connection> connections = outputLayer.get(i).getInConnection();
            for(int j = 0; j < NetworkUtils.hiddenLayerSize; j++) {
                double oldWeight = connections.get(j).getWeight();
                double newWeight = oldWeight + NetworkUtils.learningRate * errorO * connections.get(j).getLeft().getfNet();

                if (NetworkUtils.momentum)
                    newWeight += NetworkUtils.momentumRate * connections.get(j).getDeltaWeight();

                connections.get(j).setWeight(newWeight);
                connections.get(j).setDeltaWeight(newWeight - oldWeight);
            }
            //updating bias weight
            double oldBias = outputLayer.get(i).getBiasConnection().getWeight();
            double newBias = oldBias + NetworkUtils.learningRate * errorO * NetworkUtils.biasValue;
            outputLayer.get(i).getBiasConnection().setWeight(newBias);
        }

        // same for connections between hidden and input layers
        for(int i = 0; i < NetworkUtils.hiddenLayerSize; i++) {
            double errorH = NetworkUtils.costFunctionDerivative(hiddenLayer.get(i).getNet());
            double errorSum = 0;

            for(Connection connection : hiddenLayer.get(i).getOutConnection()) {
                errorSum += connection.getRight().getError() * connection.getWeight();
            }
            errorH *= errorSum;

            for(Connection connection : hiddenLayer.get(i).getInConnection()) {
                double oldWeight = connection.getWeight();
                double newWeight = oldWeight + NetworkUtils.learningRate * errorH * connection.left.getNet();

                if (NetworkUtils.momentum)
                    newWeight += NetworkUtils.momentumRate * connection.getDeltaWeight();

                connection.setWeight(newWeight);
                connection.setDeltaWeight(newWeight - oldWeight);
            }

            //updating bias
            double oldBias = hiddenLayer.get(i).getBiasConnection().getWeight();
            double newBias = oldBias + NetworkUtils.learningRate * errorH * NetworkUtils.biasValue;
            hiddenLayer.get(i).getBiasConnection().setWeight(newBias);
        }

        // sum global error
//        double globalError = 0;
//        for(int i = 0; i < outputLayer.size(); i++) {
//            globalError += Math.pow(outputLayer.get(i).getError(), 2);
//        }
    }

    private void setOutConnections(ArrayList<Neuron> neurons) {
        ArrayList<Connection> connections = neurons.get(0).getInConnection();

        for (int j = 0; j < connections.size(); j++) {
            Connection connection = connections.get(j);
            for (int k = 0; k < neurons.size(); k++) {
                connection.left.addOutConnection(neurons.get(k).getInConnection().get(j));
            }
        }
    }

    // returns global error
    private double[] runValidationSet() {
        LearningPattern pattern = readerValidationSet.getNextPattern();
        int validPrediction = 0;
        double globalError = 0;
        while(pattern != null) {
            int prediction = propagateForward(pattern, false);
            if (prediction == pattern.getExpectedClass()) {
                validPrediction++;
            }

            for(int i = 0; i < outputLayer.size(); i++) {
                double y = i == pattern.getExpectedClass() ? 1 : 0;
                double errorO = (y - outputLayer.get(i).getfNet()) *
                        NetworkUtils.costFunctionDerivative(outputLayer.get(i).getNet());
                globalError += errorO * errorO;
            }
            pattern = readerValidationSet.getNextPattern();
        }
        readerValidationSet.resetAndShuffleImages();

        double newError = globalError / 100.0 * 0.5;
        if (oldError != 0) {
            if (oldError * (1 + NetworkUtils.validationStopRate) - newError  < 0) {
                NetworkUtils.learningRunning = false;
                System.out.println(validPrediction * 100 / readerValidationSet.getImageNumber());
                System.out.println(epoch);
            }

        }
        oldError = newError;
        double effectiveness = validPrediction * 100.0 / readerValidationSet.getImageNumber();

        optionsListener.updateEffectivenessLabel(effectiveness);
        optionsListener.updateErrorLabel(newError);
        return new double[]{newError, effectiveness};
    }

    @Override
    public void startLearning() {
        NetworkUtils.learningRunning = true;
        Thread learningThread = new Thread(new Runnable() {
            public void run()
            {
                LearningPattern pattern;
                while(NetworkUtils.learningRunning){
                    for (int i = 0; i < NetworkUtils.patternSetSize; i++) {
                        pattern = readerLearningSet.getNextPattern();
                        propagateForward(pattern, true);
                    }

                    readerLearningSet.resetAndShuffleImages();

                    double[] result = runValidationSet();
                    System.out.println(String.format("%1$,.2f", result[1]) + "% - Error: " + String.format("%1$,.3f", result[0]));
                    validPredictions++;
                    epoch ++;
                }
            }});
        learningThread.start();
    }

    @Override
    public void stopLearning() {
        NetworkUtils.learningRunning = false;
    }

    @Override
    public int runNetwork(int[] image) {
        return propagateForward(new LearningPattern(image, -1, ""), false);
    }

    @Override
    public void reinitialize() {
        initialize();
    }

    @Override
    public double[] runOnValidationSet() {
        return runValidationSet();
    }

    public OptionsListener getOptionsListener() {
        return optionsListener;
    }

    public void setOptionsListener(OptionsListener optionsListener) {
        this.optionsListener = optionsListener;
    }
}