package ru.example.pnikolaev.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.example.pnikolaev.network.NeuralNetwork;
import ru.example.pnikolaev.network.NeuralNetworkBuilder;
import ru.example.pnikolaev.network.NeuralNetworkImpl;

import java.io.IOException;
import java.util.Arrays;

public class CheckHorizontalAndVerticalLinesFrom3x3 {
    static String fileName = "serfiles/CheckHorizontalAndVerticalLinesFrom3x3.ser";
    static final Logger LOGGER = LoggerFactory.getLogger(CheckHorizontalAndVerticalLinesFrom3x3.class);

    static class Training {
        public static void main(String[] args) throws IOException {

            NeuralNetwork netWork = new NeuralNetworkBuilder.Builder()
                    .setCountInput(9)
                    .setCountOutput(2)
                    .setCountHidden(9)
                    .setCountLayer(3)
                    .setCoefficientTraining(.3)
                    .setBias(true)
                    .toBuild();

            // Horizontal lines
            netWork.addTrain(new double[]{
                    1, 1, 1,
                    0, 0, 0,
                    0, 0, 0}, new double[]{1, 0});

            netWork.addTrain(new double[]{
                    0, 0, 0,
                    1, 1, 1,
                    0, 0, 0}, new double[]{1, 0});

            netWork.addTrain(new double[]{
                    0, 0, 0,
                    0, 0, 0,
                    1, 1, 1}, new double[]{1, 0});

            // Vertical lines
            netWork.addTrain(new double[]{
                    1, 0, 0,
                    1, 0, 0,
                    1, 0, 0}, new double[]{0, 1});

            netWork.addTrain(new double[]{
                    0, 1, 0,
                    0, 1, 0,
                    0, 1, 0}, new double[]{0, 1});

            netWork.addTrain(new double[]{
                    0, 0, 1,
                    0, 0, 1,
                    0, 0, 1}, new double[]{0, 1});

            // Horizontal & Vertical lines
            netWork.addTrain(new double[]{
                    1, 1, 1,
                    1, 0, 0,
                    1, 0, 0}, new double[]{1, 1});

            netWork.addTrain(new double[]{
                    1, 1, 1,
                    0, 1, 0,
                    0, 1, 0}, new double[]{1, 1});

            netWork.addTrain(new double[]{
                    1, 1, 1,
                    0, 0, 1,
                    0, 0, 1}, new double[]{1, 1});

            netWork.addTrain(new double[]{
                    1, 0, 0,
                    1, 1, 1,
                    1, 0, 0}, new double[]{1, 1});

            netWork.addTrain(new double[]{
                    0, 1, 0,
                    1, 1, 1,
                    0, 1, 0}, new double[]{1, 1});

            netWork.addTrain(new double[]{
                    0, 0, 1,
                    1, 1, 1,
                    0, 0, 1}, new double[]{1, 1});

            netWork.addTrain(new double[]{
                    1, 0, 0,
                    1, 0, 0,
                    1, 1, 1}, new double[]{1, 1});

            netWork.addTrain(new double[]{
                    0, 1, 0,
                    0, 1, 0,
                    1, 1, 1}, new double[]{1, 1});

            netWork.addTrain(new double[]{
                    0, 0, 1,
                    0, 0, 1,
                    1, 1, 1}, new double[]{1, 1});

            // No lines

            netWork.addTrain(new double[]{
                    0, 1, 1,
                    1, 1, 0,
                    1, 0, 1}, new double[]{0, 0});

            netWork.addTrain(new double[]{
                    1, 0, 1,
                    1, 1, 0,
                    0, 1, 1}, new double[]{0, 0});

            netWork.addTrain(new double[]{
                    1, 1, 0,
                    0, 1, 1,
                    1, 0, 1}, new double[]{0, 0});

            netWork.addTrain(new double[]{
                    1, 1, 0,
                    1, 1, 0,
                    0, 0, 1}, new double[]{0, 0});

            netWork.addTrain(new double[]{
                    0, 1, 1,
                    1, 0, 1,
                    1, 1, 0}, new double[]{0, 0});

            netWork.training(100000, .02);
            netWork.save(fileName);
            getAnswer(netWork);
        }
    }

    static class UseSerializable {
        public static void main(String[] args) throws IOException, ClassNotFoundException {
            NeuralNetwork netWork = NeuralNetworkImpl.init(fileName);
            getAnswer(netWork);
        }
    }

    private static void getAnswer(NeuralNetwork nn) {

        LOGGER.info(Arrays.toString(nn.getAnswer(new double[]{
                0, 0, 0,
                0, 0, 0,
                0, 0, 0})));

        LOGGER.info(Arrays.toString(nn.getAnswer(new double[]{
                0, 0, 0,
                0, 1, 0,
                0, 0, 0})));

        LOGGER.info(Arrays.toString(nn.getAnswer(new double[]{
                1, 1, 0,
                0, 1, 1,
                1, 0, 1})));

        LOGGER.info(Arrays.toString(nn.getAnswer(new double[]{
                1, 1, 0,
                1, 1, 0,
                0, 0, 0})));

        LOGGER.info(Arrays.toString(nn.getAnswer(new double[]{
                0, 0, 1,
                0, 1, 0,
                1, 0, 0})));

        LOGGER.info(Arrays.toString(nn.getAnswer(new double[]{
                1, 0, 0,
                0, 1, 0,
                0, 0, 1})));

        LOGGER.info(Arrays.toString(nn.getAnswer(new double[]{
                0, 1, 1,
                1, 0, 1,
                1, 1, 0})));

        LOGGER.info(Arrays.toString(nn.getAnswer(new double[]{
                1, 0, 1,
                1, 1, 0,
                0, 1, 1})));

    }
}
