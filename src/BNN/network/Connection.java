package BNN.network;

public class Connection {
    Neuron left;
    Neuron right;
    double weight;
    double deltaWeight;

    public Connection(Neuron left, Neuron right) {
        this.left = left;
        this.right = right;
    }

    public Neuron getLeft() {
        return left;
    }

    public void setLeft(Neuron left) {
        this.left = left;
    }

    public Neuron getRight() {
        return right;
    }

    public void setRight(Neuron right) {
        this.right = right;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getDeltaWeight() {
        return deltaWeight;
    }

    public void setDeltaWeight(double deltaWeight) {
        this.deltaWeight = deltaWeight;
    }
}
