package ru.network;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class Link implements Serializable {
    private static final long serialVersionUID = -8684841368510392034L;
    private static transient volatile ChartPanel chartPanel = null;
    private static transient XYSeriesCollection dataSet = new XYSeriesCollection();
    private Neuron inNeuron;
    private Neuron outNeuron;
    private double weight = 1;

    private transient XYSeries series;
    private transient JFrame jFrame = new JFrame();

    Link(Neuron inNeuron, Neuron outNeuron, boolean useLineChart) {
        this.inNeuron = inNeuron;
        this.outNeuron = outNeuron;
        if (outNeuron.getNeuronType().equals(NeuronType.OUTPUT)) {
            while (Math.abs(weight) > .5) {
                setWeight(Math.random() * (new Random().nextBoolean() ? 1 : -1));
            }
        } else {
            setWeight(Math.random() * (new Random().nextBoolean() ? 1 : -1));
        }
        if (useLineChart) {
            series = new XYSeries("w" + inNeuron.getNumber() + "," + outNeuron.getNumber());
            dataSet.addSeries(series);
            if (chartPanel == null) {
                synchronized (Link.class) {
                    chartPanel = initUI();
                    EventQueue.invokeLater(() -> jFrame.setVisible(true));
                }
            }
        }
    }

    private ChartPanel initUI() {
        JFreeChart chart = createChart(dataSet);
        ChartPanel cp = new ChartPanel(chart);
        cp.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jFrame.add(cp);
        jFrame.pack();
        return cp;
    }

    private JFreeChart createChart(final XYDataset dataset) {
        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                "Change Weights",
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

    void setWeight(double weight, int i, int j) {
        if (series != null && (i == 0 || i % j == 0)) {
            series.add((double) i / j, weight);
        }
        this.weight = weight;
    }
}
