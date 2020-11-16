package ru.example.pnikolaev.network;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class Link implements Serializable {
    private static final long serialVersionUID = -8684841368510392034L;
    private Neuron inNeuron;
    private Neuron outNeuron;
    private double weight = 1;

    Link(Neuron inNeuron, Neuron outNeuron) {
        this.inNeuron = inNeuron;
        this.outNeuron = outNeuron;
        inNeuron.addOutComingLinks(this);
        outNeuron.addInComingLinks(this);
        double aDouble = ThreadLocalRandom.current().nextDouble();
        if (outNeuron.getNeuronType().equals(NeuronType.OUTPUT)) {
            setWeight(aDouble - 0.5);
        } else {
            setWeight(aDouble * (ThreadLocalRandom.current().nextBoolean() ? 1 : -1));
        }
    }

    Neuron getInNeuron() {
        return inNeuron;
    }

    Neuron getOutNeuron() {
        return outNeuron;
    }

    double getWeight() {
        return weight;
    }

    void setWeight(double weight) {
        this.weight = weight;
    }
}
