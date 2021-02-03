package ru.example.pnikolaev.functions;

import java.io.Serializable;

public abstract class FActivation implements Serializable {

    private static final long serialVersionUID = -51967861508205891L;

    public abstract double get(double v);
}
