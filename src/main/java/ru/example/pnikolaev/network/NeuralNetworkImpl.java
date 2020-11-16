package ru.example.pnikolaev.network;

import ru.example.pnikolaev.functions.FError;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NeuralNetworkImpl implements NeuralNetwork, Serializable {
    private static final long serialVersionUID = 6561671168938651613L;
    private transient List<double[]> trainSet = new ArrayList<>();
    private transient double cTraining;
    private transient FError fError;
    private List<List<Neuron>> neurons;
    private List<Neuron> first;
    private List<Neuron> last;

    NeuralNetworkImpl(List<List<Neuron>> neurons, FError fError, double cTraining) {
        this.neurons = neurons;
        first = neurons.get(0).stream()
                .filter(x -> !x.isBias())
                .collect(Collectors.toList());
        last = neurons.get(neurons.size() - 1);
        this.fError = fError;
        this.cTraining = cTraining;
    }

    public void save(String fileName) throws IOException {
        // Serialization
        try (FileOutputStream file = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(file)) {
            out.writeObject(this);
        }
    }

    public static NeuralNetworkImpl init(String fileName) throws IOException, ClassNotFoundException {
        // Deserialization
        try (FileInputStream file = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(file)) {
            return (NeuralNetworkImpl) in.readObject();
        }
    }

    private void calInputsAndOutputs() {
        for (int i = 1; i < neurons.size(); i++) {
            neurons.get(i).forEach(Neuron::calOutput);
        }
    }

    private void calNeuronsError() {
        neurons.get(neurons.size() - 1).forEach(Neuron::calErrorOutput);
        for (int i = neurons.size() - 2; i >= 1; i--) {
            neurons.get(i).forEach(Neuron::calError);
        }
    }

    private void calNewWeightForLinks() {
        for (int i = neurons.size() - 1; i >= 0; i--) {
            neurons.get(i)
                    .forEach(neuron -> neuron.getInComingLinks()
                            .forEach(link -> calNewWeightForLink(link, neuron)));
        }
    }

    private void calNewWeightForLink(Link link, Neuron neuron) {
        double shift = cTraining * neuron.getError() * link.getInNeuron().getOutput();
        link.setWeight(link.getWeight() + shift);
    }

    private double calError() {
        return fError.get(neurons.get(neurons.size() - 1));
    }

    public void addTrain(double[] values, double[] targetOutValues) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("");
        }
        if (targetOutValues == null || targetOutValues.length == 0) {
            throw new IllegalArgumentException("");
        }
        if (values.length != first.size() || targetOutValues.length != last.size()) {
            throw new IllegalArgumentException("");
        }
        trainSet.add(values);
        trainSet.add(targetOutValues);
    }

    public void training(int counters, double minError) {
        if (0 > counters) {
            throw new IllegalArgumentException("counters");
        }
        if (0 > minError) {
            throw new IllegalArgumentException("minError");
        }
        double error;
        for (int i = 0; i < counters; i++) {
            error = executeTrainSet();
            if (Math.abs(error) < minError) {
                return;
            }
        }
    }

    public double executeTrainSet() {
        double errorSum = 0;
        for (int j = 0; j < trainSet.size() - 1; j++) {
            errorSum += backPropagation(trainSet.get(j), trainSet.get(j + 1));
            j++;
        }
        return errorSum / trainSet.size() * 2;
    }

    public double backPropagation(double[] inputs, double[] target) {
        setInputAndTargetValues(inputs, target);
        calInputsAndOutputs();
        calNeuronsError();
        double error = calError();
        calNewWeightForLinks();
        return error;
    }

    public double[] getAnswer(double[] values) {
        if (values.length != first.size()) {
            throw new IllegalArgumentException("");
        }
        setInputValues(values);
        calInputsAndOutputs();
        return neurons.get(neurons.size() - 1).stream().mapToDouble(Neuron::getOutput).toArray();
    }

    private void setInputAndTargetValues(double[] values, double[] targetOutValues) {
        setInputValues(values);
        for (int i = 0; i < last.size(); i++) {
            last.get(i).setTarget(targetOutValues[i]);
        }
    }

    private void setInputValues(double[] values) {
        for (int i = 0; i < first.size(); i++) {
            first.get(i).setOutput(values[i]);
        }
    }

    public List<List<Neuron>> getNeurons() {
        return Collections.unmodifiableList(neurons);
    }
}
