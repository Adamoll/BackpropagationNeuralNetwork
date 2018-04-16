package BNN;

import java.util.ArrayList;

public class Neuron {
    private double net;
    private double fNet;
    private double error;
    private ArrayList<Connection> inConnection;
    private ArrayList<Connection> outConnection;
    private Connection biasConnection;

    public Neuron() {
        inConnection = new ArrayList<>();
        outConnection = new ArrayList<>();
    }

    public void addInConnections(ArrayList<Neuron> inNeurons) {
        for(Neuron n : inNeurons) {
            Connection connection = new Connection(n, this);
            inConnection.add(connection);
        }
    }

    public void addBiasConnection(Neuron bias) {
        Connection connection = new Connection(bias, this);
        biasConnection = connection;
        inConnection.add(connection);
    }

    public void addOutConnection(Connection connection) {
        this.outConnection.add(connection);
    }

    public double getNet() {
        return net;
    }

    public void setNet(double net) {
        this.net = net;
    }

    public ArrayList<Connection> getInConnection() {
        return inConnection;
    }

    public double getfNet() {
        return fNet;
    }

    public void setfNet(double fNet) {
        this.fNet = fNet;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public ArrayList<Connection> getOutConnection() {
        return outConnection;
    }

    public Connection getBiasConnection() {
        return biasConnection;
    }

    public void setBiasConnection(Connection biasConnection) {
        this.biasConnection = biasConnection;
    }

    public void setOutConnection(ArrayList<Connection> outConnection) {
        this.outConnection = outConnection;
    }

    public void setInConnection(ArrayList<Connection> inConnection) {
        this.inConnection = inConnection;
    }
}
