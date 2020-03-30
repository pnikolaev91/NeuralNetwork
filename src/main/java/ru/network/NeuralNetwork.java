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
import java.util.LinkedList;
import java.util.List;

public class NeuralNetwork implements Serializable {
    private static final long serialVersionUID = 6561671168938651613L;
    LinkedList<List<Neuron>> neurons;
    private transient LinkedList<double[]> trainSet = new LinkedList<>();
    private transient double cTraining;
    private transient FError fError;
    private transient XYSeries series;
    private transient JFrame jFrame = new JFrame();

    public NeuralNetwork(List<List<Neuron>> neurons, FError fError, double cTraining, boolean useLineChart) {
        this.neurons = (LinkedList<List<Neuron>>) neurons;
        this.fError = fError;
        this.cTraining = cTraining;
        if (useLineChart) {
            series = new XYSeries("Error");
            initUI();
            EventQueue.invokeLater(() -> jFrame.setVisible(true));
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
        for (List<Neuron> n : neurons) {
            n.parallelStream().forEach(Neuron::calInput);
            n.parallelStream().forEach(Neuron::calOutput);
        }
    }

    private void calNeuronsError() {
        for (int i = neurons.size() - 1; i >= 0; i--) {
            neurons.get(i).parallelStream().forEach(Neuron::calError);
        }
    }

    private void changeWeights(int iteration, int j) {
        for (int i = neurons.size() - 1; i >= 0; i--) {
            neurons.get(i).parallelStream()
                    .forEach(neuron -> neuron.getInComingLinks()
                            .parallelStream()
                            .forEach(l -> l.setWeight(l.getWeight() + cTraining * neuron.getError() * l.getInNeuron().getOutput(), iteration, j)));

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
        for (int i = 0; i < counters; i++) {
            double errorSum = 0;
            for (int j = 0; j < trainSet.size() - 1; j++) {
                setInputAndTargetValues(trainSet.get(j), trainSet.get(j + 1));
                calInputsAndOutputs();
                calNeuronsError();
                errorSum += calError();
                changeWeights(i * trainSet.size() + j, trainSet.size());
                j++;
            }
            double v = errorSum / trainSet.size() * 2;
            if (series != null) {
                series.add(i, v * 100);
            }
            error = Math.min(error, v);
            if (Math.abs(v) < minError) {
                return;
            }
        }
    }

    public double[] getAnswer(double[] values) {
        setInputValues(values);
        calInputsAndOutputs();
        return neurons.getLast().stream().mapToDouble(Neuron::getOutput).toArray();
    }

    private void setInputAndTargetValues(double[] values, double[] targetOutValues) {
        setInputValues(values);
        List<Neuron> last = neurons.getLast();
        if (targetOutValues.length != last.size()) {
            throw new IllegalArgumentException("");
        }

        for (int i = 0; i < last.size(); i++) {
            last.get(i).setTarget(targetOutValues[i]);
        }
    }

    private void setInputValues(double[] values) {
        List<Neuron> first = neurons.getFirst();
        long count = neurons.getFirst().stream().filter(x -> !x.isBias()).count();
        if (values.length != count) {
            throw new IllegalArgumentException("");
        }

        for (int i = 0; i < count; i++) {
            first.get(i).setOutput(values[i]);
        }
    }
}
