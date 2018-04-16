package BNN;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

public class NetworkUtils {
    public static int inputLayerSize = 70;
    public static int outputLayerSize = 10;
    public static int hiddenLayerSize = 26;
    public static int patternSetSize = 1000;

    public static double beta = 3;
    public static double learningRate = 0.005;
    public static double biasValue = 1;
    public static double minWeight = -0.1;
    public static double maxWeight = 0.1;
    public static double momentumRate = 0.4;
    public static double validationStopRate = 0.15;

    public static boolean learningRunning = false;
    public static boolean momentum = true;

    public static ThreadLocalRandom random = ThreadLocalRandom.current();

    public static double getRandomDouble() {
        return random.nextDouble(minWeight, maxWeight);
    }

    // cost function assumed as binary sigmoidal function
    public static double costFunction(double net) {
        return (1 - Math.pow(Math.E, -beta * net)) / (1 + Math.pow(Math.E, -beta * net));
    }

    public static double costFunctionDerivative(double net) {
        double c = costFunction(net);
        return beta * (1 - Math.pow(c, 2));
    }

    // shuffling patterns using Fisher–Yates shuffle algorithm
    public static void shuffleArray(File[] array)
    {
        int index;
        File temp;
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
