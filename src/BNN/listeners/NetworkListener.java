package BNN.listeners;

public interface NetworkListener {
    void startLearning();
    void stopLearning();
    int runNetwork(int[] image);
    void reinitialize();
    double[] runOnValidationSet();
}
