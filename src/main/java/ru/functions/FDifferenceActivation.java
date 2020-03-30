package ru.functions;

import ru.network.Neuron;

import java.io.Serializable;

public abstract class FDifferenceActivation implements Serializable {

    private static final long serialVersionUID = 3823360636279468186L;

    public abstract double get(Neuron neuron);
}
