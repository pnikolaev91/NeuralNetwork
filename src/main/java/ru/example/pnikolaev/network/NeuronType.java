package ru.example.pnikolaev.network;

public enum NeuronType {
    INPUT("input"),
    OUTPUT("output"),
    HIDDEN("hidden"),
    BIAS("bias");

    private String value;

    NeuronType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}