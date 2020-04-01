package ru.network;

import ru.functions.FActivation;
import ru.functions.FDifferenceActivation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Neuron implements Serializable {
    private static final long serialVersionUID = 7814835293195643241L;
    private static transient int countNeurons = 0;
    private int number = 0;
    private int numberOnLayer = 0;
    //Все входы нейрона
    private List<Link> inComingLinks = new ArrayList<>();
    //Все выходы нейрона
    private List<Link> outComingLinks = new ArrayList<>();
    // Функция активации
    public FActivation fActivation;
    // Производная от функции активации
    private FDifferenceActivation fDifferenceActivation;
    private NeuronType neuronType;
    private double input;
    private double output;
    private transient double target;
    private transient double error;
    private transient BufferedImage bufferedImage;

    Neuron(NeuronType neuronType, FActivation fActivation, FDifferenceActivation fDifferenceActivation, int numberOnLayer) {
        number = countNeurons++;
        this.fActivation = fActivation;
        this.neuronType = neuronType;
        this.fDifferenceActivation = fDifferenceActivation;
        this.numberOnLayer = numberOnLayer;
    }

    public int getNumberOnLayer() {
        return numberOnLayer;
    }

    void createBufferedImage(int width, int height) {
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    void saveBufferedImage(int era) {
        try {
            ImageIO.write(bufferedImage, "jpg", new File("images/neurons/number-" + number + "-" + era + ".jpg"));
        } catch (IOException ignore) {
        }
        bufferedImage = null;
    }

    void setPixel(int x, int y) {
        bufferedImage.setRGB(x, bufferedImage.getHeight() - y - 1, Color.BLUE.getRGB() | ((int) (getOutput() * 255) << 8) | ((int) (getOutput() * 255) << 16));
    }

    void setPixel(int x, int y, int rgb) {
        bufferedImage.setRGB(x, bufferedImage.getHeight() - y - 1, rgb);
    }

    NeuronType getNeuronType() {
        return neuronType;
    }

    double getError() {
        return error;
    }

    boolean isBias() {
        return NeuronType.BIAS.equals(neuronType);
    }

    double getInput() {
        return input;
    }

    double getOutput() {
        return output;
    }

    void calInput() {
        if (!inComingLinks.isEmpty()) {
            input = getInComingLinks()
                    .stream()
                    .mapToDouble(l -> l.getWeight() * l.getInNeuron().getOutput())
                    .sum();
        }
    }

    void calOutput() {
        if (!inComingLinks.isEmpty()) {
            output = fActivation.get(this);
        }
    }

    void calError() {
        if (!inComingLinks.isEmpty()) {
            if (neuronType.equals(NeuronType.OUTPUT)) {
                error = (target - output) * fDifferenceActivation.get(this);
            } else {
                error = fDifferenceActivation.get(this) * getOutComingLinks().parallelStream()
                        .mapToDouble(l -> l.getWeight() * l.getOutNeuron().getError())
                        .sum();
            }
        }
    }

    void addInComingLinks(Link link) {
        inComingLinks.add(link);
    }

    void addOutComingLinks(Link link) {
        outComingLinks.add(link);
    }

    List<Link> getInComingLinks() {
        return inComingLinks;
    }

    List<Link> getOutComingLinks() {
        return outComingLinks;
    }

    void setOutput(double output) {
        this.output = output;
    }

    void setTarget(double target) {
        this.target = target;
    }

    double getTarget() {
        return target;
    }
}
