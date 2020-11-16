package ru.example.pnikolaev.network;

import java.io.IOException;
import java.util.List;

public interface NeuralNetwork {

    double[] getAnswer(double[] values);

    double backPropagation(double[] inputs, double[] target);

    void training(int counters, double minError);

    void addTrain(double[] values, double[] targetOutValues);

    void save(String fileName) throws IOException;

    double executeTrainSet();

    List<List<Neuron>> getNeurons();
}
