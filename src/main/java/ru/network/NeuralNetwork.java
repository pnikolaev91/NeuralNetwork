package ru.network;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ru.functions.FError;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NeuralNetwork implements Serializable {
    private static final long serialVersionUID = 6561671168938651613L;
    LinkedList<List<Neuron>> neurons;
    private transient LinkedList<double[]> trainSet = new LinkedList<>();
    private transient double cTraining;
    private transient FError fError;
    private transient XYSeries series;
    private transient JFrame jFrame = new JFrame();
    private ArrayList<Neuron> first;
    private ArrayList<Neuron> last;

    public NeuralNetwork(List<List<Neuron>> neurons, FError fError, double cTraining, boolean useLineChart) {
        this.neurons = (LinkedList<List<Neuron>>) neurons;
        first = ((LinkedList<List<Neuron>>) neurons).getFirst().stream().filter(x -> !x.isBias())
                .collect(Collectors.toCollection(ArrayList::new));
        last = (ArrayList<Neuron>) ((LinkedList<List<Neuron>>) neurons).getLast();
        this.fError = fError;
        this.cTraining = cTraining;
        if (useLineChart) {
            series = new XYSeries("Error");
            initUI();
            EventQueue.invokeLater(() -> {
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.setVisible(true);
            });
        }
    }

    public void save(String fileName) throws IOException {
        // Serialization
        try (FileOutputStream file = new FileOutputStream(fileName)) {
            try (ObjectOutputStream out = new ObjectOutputStream(file)) {
                out.writeObject(this);
            }
        }
    }

    public static NeuralNetwork init(String fileName) throws IOException, ClassNotFoundException {
        // Deserialization
        try (FileInputStream file = new FileInputStream(fileName)) {
            try (ObjectInputStream in = new ObjectInputStream(file)) {
                return (NeuralNetwork) in.readObject();
            }
        }
    }

    private void initUI() {
        XYDataset dataSet = createDataSet();
        JFreeChart chart = createChart(dataSet);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jFrame.add(chartPanel);
        jFrame.pack();
    }

    private XYDataset createDataSet() {
        XYSeriesCollection dataSet = new XYSeriesCollection();
        dataSet.addSeries(series);
        return dataSet;
    }

    private JFreeChart createChart(final XYDataset dataset) {
        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                "MSE",
                "Era",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        XYPlot plot = xyLineChart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        return xyLineChart;
    }

    private void calInputsAndOutputs() {
        for (int i = 1; i < neurons.size(); i++) {
            neurons.get(i).forEach(Neuron::calOutput);
        }
    }

    private void calNeuronsError() {
        neurons.getLast().forEach(Neuron::calErrorOutput);
        for (int i = neurons.size() - 2; i >= 1; i--) {
            neurons.get(i).forEach(Neuron::calError);
        }
    }

    private void changeWeights() {
        for (int i = neurons.size() - 1; i >= 0; i--) {
            neurons.get(i).forEach(neuron -> neuron.getInComingLinks()
                    .forEach(l -> l.setWeight(l.getWeight() + cTraining * neuron.getError() * l.getInNeuron().getOutput())));
        }
    }

    private double calError() {
        return fError.get(neurons.getLast());
    }

    public void addTrain(double[] values, double[] targetOutValues) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("");
        }
        if (targetOutValues == null || targetOutValues.length == 0) {
            throw new IllegalArgumentException("");
        }
        trainSet.add(values);
        trainSet.add(targetOutValues);
    }

    public void training(int counters, double minError) {
        double error = 100;
        checkTrainSet();
        for (int i = 0; i < (counters != 0 ? counters : 1000000); i++) {
            double errorSum = 0;
            for (int j = 0; j < trainSet.size() - 1; j++) {
                setInputAndTargetValues(trainSet.get(j), trainSet.get(j + 1));
                calInputsAndOutputs();
                calNeuronsError();
                errorSum += calError();
                changeWeights();
                j++;
            }
            double v = errorSum / trainSet.size() * 2;
            if (series != null) {
                series.add(i, v * 100);
            }
            error = Math.min(error, v);
            if (Math.abs(v) < minError || counters - 1 == i) {
                return;
            }
        }
    }

    public void backpropagation(double[] inputs, double[] target) {
        setInputAndTargetValues(inputs, target);
        calInputsAndOutputs();
        calNeuronsError();
        changeWeights();
    }

    public double[] getAnswer(double[] values) {
        if (values.length != first.size()) {
            throw new IllegalArgumentException("");
        }
        setInputValues(values);
        calInputsAndOutputs();
        return neurons.getLast().stream().mapToDouble(Neuron::getOutput).toArray();
    }

    private void checkTrainSet() {
        for (int i = 0; i < trainSet.size(); i++) {
            if (trainSet.get(i).length != first.size() || trainSet.get(i + 1).length != last.size()) {
                throw new IllegalArgumentException("");
            }
            i++;
        }
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
}
