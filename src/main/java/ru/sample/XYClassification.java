package ru.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.network.NeuralNetwork;
import ru.network.NeuralNetworkBuilder;

import java.io.IOException;
import java.util.Arrays;

public class XYClassification {
    static String fileName = "serfiles/XYClassification.ser";
    static final Logger LOGGER = LoggerFactory.getLogger(XYClassification.class);

    static class Training {
        public static void main(String[] args) throws IOException {
            NeuralNetwork netWork = new NeuralNetworkBuilder.Builder()
                    .setUseLineChart(true)
                    .setCountInput(2)
                    .setCountOutput(1)
                    .setCountHidden(0)
                    .setCountLayer(2)
                    .setCoefficientTraining(0.03)
                    .setBias(true).toBuild();

            netWork.addTrain(new double[]{2, 11}, new double[]{0});
            netWork.addTrain(new double[]{3, 11}, new double[]{0});
            netWork.addTrain(new double[]{3, 12}, new double[]{0});
            netWork.addTrain(new double[]{4, 12}, new double[]{0});
            netWork.addTrain(new double[]{4, 13}, new double[]{0});

            netWork.addTrain(new double[]{6, 22}, new double[]{1});
            netWork.addTrain(new double[]{7, 23}, new double[]{1});
            netWork.addTrain(new double[]{8, 24}, new double[]{1});
            netWork.addTrain(new double[]{6, 25}, new double[]{1});
            netWork.addTrain(new double[]{7, 25}, new double[]{1});

            netWork.training(5000, 0.02);
            netWork.save(fileName);

            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{7, 28})));
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{8, 24})));

            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{2, 11})));
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{4, 13})));
        }
    }

    static class UseSerializable {
        public static void main(String[] args) throws IOException, ClassNotFoundException {
            NeuralNetwork netWork = NeuralNetwork.init(fileName);
            //Red
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{4, 3})));
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{5, 1})));
            //Blue
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{1, 2})));
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{2, 5})));
        }
    }

    static class Training1 {
        public static void main(String[] args) throws IOException {
            NeuralNetwork netWork = new NeuralNetworkBuilder.Builder()
                    .setUseLineChart(true)
                    .setCountInput(2)
                    .setCountOutput(1)
                    .setCountHidden(4)
                    .setCountLayer(3)
                    .setBias(true).toBuild();
            //Red (x,y)
            netWork.addTrain(new double[]{1 + 5, 2 + 5}, new double[]{0});
            netWork.addTrain(new double[]{2 + 5, 1 + 5}, new double[]{0});
            netWork.addTrain(new double[]{2 + 5, -1 + 5}, new double[]{0});
            netWork.addTrain(new double[]{1 + 5, -2 + 5}, new double[]{0});
            netWork.addTrain(new double[]{-1 + 5, -2 + 5}, new double[]{0});
            netWork.addTrain(new double[]{-2 + 5, -1 + 5}, new double[]{0});
            netWork.addTrain(new double[]{-2 + 5, 1 + 5}, new double[]{0});
            netWork.addTrain(new double[]{-1 + 5, 2 + 5}, new double[]{0});
            //Blue (x,y)
            netWork.addTrain(new double[]{2 + 5, 4 + 5}, new double[]{1});
            netWork.addTrain(new double[]{4 + 5, 2 + 5}, new double[]{1});
            netWork.addTrain(new double[]{4 + 5, -2 + 5}, new double[]{1});
            netWork.addTrain(new double[]{2 + 5, -4 + 5}, new double[]{1});
            netWork.addTrain(new double[]{-2 + 5, -4 + 5}, new double[]{1});
            netWork.addTrain(new double[]{-4 + 5, -2 + 5}, new double[]{1});
            netWork.addTrain(new double[]{-4 + 5, 2 + 5}, new double[]{1});
            netWork.addTrain(new double[]{-2 + 5, 4 + 5}, new double[]{1});
            netWork.training(10000, 0.02);
            netWork.save(fileName);
            //Red
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{-2 + 5, 1 + 5})));
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{1 + 5, -2 + 5})));
            //Blue
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{2 + 5, -4 + 5})));
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{-2 + 5, 4 + 5})));
        }
    }
}
