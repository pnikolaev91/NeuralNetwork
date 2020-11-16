package ru.example.pnikolaev.network;

import ru.example.pnikolaev.functions.FActivation;
import ru.example.pnikolaev.functions.FDifferenceActivation;
import ru.example.pnikolaev.functions.FError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NeuralNetworkBuilder {

    private NeuralNetworkBuilder() {
    }

    public static class Builder implements Serializable {

        private static final long serialVersionUID = 2035167839116595998L;
        private int countLayer = 2;
        private int countInput = 1;
        private int countOutput = 1;
        private int countHidden = 0;
        private boolean bias = true;
        private boolean monitoring = false;

        private transient double cTraining = .1;
        // Функция активации (Сигмоид)
        private FActivation fActivation = new FActivation() {
            @Override
            public double get(double v) {
                return 1 / (1 + Math.exp(-v));
            }
        };
        // Логистическая функция для сигмойда
        private FDifferenceActivation fDifferenceActivation = new FDifferenceActivation() {
            @Override
            public double get(double v) {
                return (1 - v) * v;
            }
        };
        // Функция ошибки
        private FError fError = new FError() {
            @Override
            public double get(List<Neuron> list) {
                return list.stream().mapToDouble(n -> Math.pow(n.getTarget() - n.getOutput(), 2) / list.size()).sum();
            }
        };

        public Builder setCoefficientTraining(double cTraining) {
            this.cTraining = cTraining;
            return this;
        }


        public Builder setFError(FError fError) {
            this.fError = fError;
            return this;
        }

        public Builder setBias(boolean bias) {
            this.bias = bias;
            return this;
        }

        public Builder setCountLayer(int countLayer) {
            this.countLayer = Math.max(countLayer, 2);
            return this;
        }

        public Builder setCountInput(int countInput) {
            this.countInput = Math.max(countInput, 1);
            return this;
        }

        public Builder setCountOutput(int countOutput) {
            this.countOutput = Math.max(countOutput, 1);
            return this;
        }

        public Builder setFActivation(FActivation fActivation) {
            this.fActivation = fActivation;
            return this;
        }

        public Builder setFDifferenceActivation(FDifferenceActivation fDifferenceActivation) {
            this.fDifferenceActivation = fDifferenceActivation;
            return this;
        }

        public Builder setCountHidden(int countHidden) {
            this.countHidden = Math.max(countHidden, 0);
            return this;
        }

        public Builder setMonitoring(boolean monitoring) {
            this.monitoring = monitoring;
            return this;
        }

        public NeuralNetwork toBuild() {
            ArrayList<List<Neuron>> list = new ArrayList<>();
            list.add(createNeurons(countInput, true, NeuronType.INPUT));
            createHiddenLayers(list);
            list.add(createNeurons(countOutput, false, NeuronType.OUTPUT));
            createLinks(list);
            NeuralNetworkImpl nn = new NeuralNetworkImpl(list, fError, cTraining);
            return monitoring ? new NeuralNetworkMonitorMSE(nn) : nn;
        }

        private void createHiddenLayers(List<List<Neuron>> list) {
            if (countHidden == 0 || countLayer == 2) {
                return;
            }
            if (countLayer == 3) {
                list.add(createNeurons(countHidden, true, NeuronType.HIDDEN));
                return;
            }
            int countHiddenLayer = countLayer - 2;

            int addedNeurons = 0;
            int buf;
            for (int i = 0; i < countHiddenLayer; i++) {
                if ((countHidden - addedNeurons) % (countHiddenLayer - i) != 0) {
                    addedNeurons += buf = (int) Math.ceil((double) (countHidden - addedNeurons) / (countHiddenLayer - i));
                } else {
                    addedNeurons += buf = (countHidden - addedNeurons) / (countHiddenLayer - i);
                }
                list.add(createNeurons(buf, true, NeuronType.HIDDEN));
            }
        }

        private void createLinks(List<List<Neuron>> list) {
            for (int i = 0; i < list.size() - 1; i++) {
                for (Neuron neuron : list.get(i)) {
                    for (Neuron neuron1 : list.get(i + 1).stream().filter(n -> !n.isBias()).collect(Collectors.toList())) {
                        new Link(neuron, neuron1);
                    }
                }
            }
        }

        private List<Neuron> createNeurons(int count, boolean allowedBias, NeuronType neuronType) {
            ArrayList<Neuron> neurons = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                neurons.add(new Neuron(neuronType, fActivation, fDifferenceActivation));
            }
            if (bias && allowedBias) {
                Neuron neuron = new Neuron(NeuronType.BIAS, fActivation, fDifferenceActivation);
                neuron.setOutput(1);
                neurons.add(neuron);
            }
            return neurons;
        }
    }
}
