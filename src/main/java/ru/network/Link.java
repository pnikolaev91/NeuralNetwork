package ru.network;

import java.io.Serializable;
import java.util.Random;

public class Link implements Serializable {
    private static final long serialVersionUID = -8684841368510392034L;
    private Neuron inNeuron;
    private Neuron outNeuron;
    private double weight = 1;

    Link(Neuron inNeuron, Neuron outNeuron) {
        this.inNeuron = inNeuron;
        this.outNeuron = outNeuron;
        if (outNeuron.getNeuronType().equals(NeuronType.OUTPUT)) {
            setWeight(Math.random() - 0.5);
        } else {
            setWeight(Math.random() * (new Random().nextBoolean() ? 1 : -1));
        }
    }

    Neuron getInNeuron() {
        return inNeuron;
    }

    void setInNeuron(Neuron inNeuron) {
        this.inNeuron = inNeuron;
    }

    Neuron getOutNeuron() {
        return outNeuron;
    }

    void setOutNeuron(Neuron outNeuron) {
        this.outNeuron = outNeuron;
    }

    double getWeight() {
        return weight;
    }

    void setWeight(double weight) {
        this.weight = weight;
    }
}
