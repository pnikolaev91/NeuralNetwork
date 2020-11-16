package ru.example.pnikolaev.network;

import ru.example.pnikolaev.functions.FActivation;
import ru.example.pnikolaev.functions.FDifferenceActivation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Neuron implements Serializable {
    private static final long serialVersionUID = 7814835293195643241L;
    //Все входы нейрона
    private List<Link> inComingLinks = new ArrayList<>();
    //Все выходы нейрона
    private List<Link> outComingLinks = new ArrayList<>();
    // Функция активации
    public FActivation fActivation;
    // Производная от функции активации
    private FDifferenceActivation fDifferenceActivation;
    private NeuronType neuronType;
    private double input;
    private double output;
    private transient double target;
    private transient double error;

    Neuron(NeuronType neuronType, FActivation fActivation, FDifferenceActivation fDifferenceActivation) {
        this.neuronType = neuronType;
        this.fActivation = fActivation;
        this.fDifferenceActivation = fDifferenceActivation;
    }

    NeuronType getNeuronType() {
        return neuronType;
    }

    double getError() {
        return error;
    }

    boolean isBias() {
        return NeuronType.BIAS.equals(neuronType);
    }

    double getInput() {
        return input;
    }

    double getOutput() {
        return output;
    }

    void calOutput() {
        if (!inComingLinks.isEmpty()) {
            input = getInComingLinks()
                    .stream()
                    .mapToDouble(l -> l.getWeight() * l.getInNeuron().getOutput())
                    .sum();
            output = fActivation.get(input);
        }
    }

    void calErrorOutput() {
        error = (target - output) * fDifferenceActivation.get(output);
    }

    void calError() {
        if (!inComingLinks.isEmpty()) {
            error = fDifferenceActivation.get(output) * getOutComingLinks().stream()
                    .mapToDouble(l -> l.getWeight() * l.getOutNeuron().getError())
                    .sum();
        }
    }

    void addInComingLinks(Link link) {
        inComingLinks.add(link);
    }

    void addOutComingLinks(Link link) {
        outComingLinks.add(link);
    }

    List<Link> getInComingLinks() {
        return inComingLinks;
    }

    List<Link> getOutComingLinks() {
        return outComingLinks;
    }

    void setOutput(double output) {
        this.output = output;
    }

    void setTarget(double target) {
        this.target = target;
    }

    double getTarget() {
        return target;
    }
}
