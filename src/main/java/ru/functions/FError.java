package ru.functions;

import ru.network.Neuron;

import java.io.Serializable;
import java.util.List;

public abstract class FError implements Serializable {

    private static final long serialVersionUID = -5218027898665388376L;

    public abstract double get(List<Neuron> list);
}
