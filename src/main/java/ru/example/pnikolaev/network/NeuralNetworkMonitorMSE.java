package ru.example.pnikolaev.network;

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
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NeuralNetworkMonitorMSE extends Thread implements NeuralNetwork {
    private final XYSeries series;
    private final JFrame jFrame = new JFrame();
    private final NeuralNetwork nn;

    NeuralNetworkMonitorMSE(NeuralNetwork nn) {
        this.nn = nn;
        //TODO
        series = new XYSeries("Error");
        initUI();
        EventQueue.invokeLater(() -> {
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setVisible(true);
        });
        start();
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

    private volatile AtomicInteger count = new AtomicInteger();
    private volatile double error;

    @Override
    public void run() {
        int buf = 0;
        while (true) {
            int v = count.get();
            if (buf != v) {
                series.add(buf = v, error * 100);
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    series.add(count.get(), error * 100);
                    return;
                }
            }
        }
    }

    @Override
    public double[] getAnswer(double[] values) {
        return nn.getAnswer(values);
    }

    @Override
    public double backPropagation(double[] inputs, double[] target) {
        return nn.backPropagation(inputs, target);
    }

    @Override
    public void training(int counters, double minError) {
        if (0 > counters) {
            throw new IllegalArgumentException("counters");
        }
        if (0 > minError) {
            throw new IllegalArgumentException("minError");
        }
        for (count.set(0); count.get() < counters; count.getAndIncrement()) {
            error = executeTrainSet();
            if (Math.abs(error) < minError) {
                break;
            }
        }
        interrupt();
    }

    @Override
    public void addTrain(double[] values, double[] targetOutValues) {
        nn.addTrain(values, targetOutValues);
    }

    @Override
    public void save(String fileName) throws IOException {
        nn.save(fileName);
    }

    @Override
    public double executeTrainSet() {
        return nn.executeTrainSet();
    }

    @Override
    public List<List<Neuron>> getNeurons() {
        return nn.getNeurons();
    }
}
