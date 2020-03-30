package ru.network;

import ru.functions.FActivation;
import ru.functions.FDifferenceActivation;
import ru.functions.FError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NeuralNetworkBuilder {

    private NeuralNetworkBuilder() {
    }

    public static class Builder implements Serializable {

        private static final long serialVersionUID = 2035167839116595998L;
        private boolean useLineChart = false;
        private int countLayer = 2;
        private int countInput = 1;
        private int countOutput = 1;
        private int countHidden = 0;
        private boolean bias = false;
        private transient double cTraining = .8;
        // Функция активации (Сигмоид)
        private FActivation fActivation = new FActivation() {
            @Override
            public double get(Neuron n) {
                return 1 / (1 + Math.exp(-n.getInput()));
            }
        };
        // Логистическая функция для сигмойда
        private FDifferenceActivation fDifferenceActivation = new FDifferenceActivation() {
            @Override
            public double get(Neuron n) {
                return (1 - n.getOutput()) * n.getOutput();
            }
        };
        // Функция ошибки
        private FError fError = new FError() {
            @Override
            public double get(List<Neuron> list) {
                return list.parallelStream().mapToDouble(n -> Math.pow(n.getTarget() - n.getOutput(), 2) / list.size()).sum();
            }
        };

        public void setCoefficientTraining(double cTraining) {
            this.cTraining = cTraining;
        }

        public void setUseLineChart(boolean useLineChart) {
            this.useLineChart = useLineChart;
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

        public void setFActivation(FActivation fActivation) {
            this.fActivation = fActivation;
        }

        public void setFDifferenceActivation(FDifferenceActivation fDifferenceActivation) {
            this.fDifferenceActivation = fDifferenceActivation;
        }

        public Builder setCountHidden(int countHidden) {
            this.countHidden = Math.max(countHidden, 0);
            return this;
        }

        public NeuralNetwork toBuild() {
            LinkedList<List<Neuron>> list = new LinkedList<>();
            list.add(createNeurons(countInput, true, NeuronType.INPUT));
            createHiddenLayers(list);
            list.add(createNeurons(countOutput, false, NeuronType.OUTPUT));
            createLinks(list);
            return new NeuralNetwork(list, fError, cTraining, useLineChart);
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
                        Link link = new Link(neuron, neuron1, useLineChart);
                        neuron.addOutComingLinks(link);
                        neuron1.addInComingLinks(link);
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
