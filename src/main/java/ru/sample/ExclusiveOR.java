package ru.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.network.NeuralNetwork;
import ru.network.NeuralNetworkBuilder;

import java.io.IOException;
import java.util.Arrays;

public class ExclusiveOR {
    static String fileName = "serfiles/ExclusiveOR.ser";
    static final Logger LOGGER = LoggerFactory.getLogger(ExclusiveOR.class);

    static class Training {
        public static void main(String[] args) throws IOException {
            NeuralNetwork netWork = new NeuralNetworkBuilder.Builder()
                    .setUseLineChart(true)
                    .setCountInput(2)
                    .setCountOutput(1)
                    .setCountHidden(2)
                    .setCountLayer(3)
                    .setCoefficientTraining(.8)
                    .setBias(true).toBuild();
            netWork.addTrain(new double[]{0, 0}, new double[]{0});
            netWork.addTrain(new double[]{0, 1}, new double[]{1});
            netWork.addTrain(new double[]{1, 0}, new double[]{1});
            netWork.addTrain(new double[]{1, 1}, new double[]{0});
            netWork.training(10000, 0.002);
            netWork.save(fileName);
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{1, 0})));
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{1, 1})));
        }
    }

    static class UseSerializable {
        public static void main(String[] args) throws IOException, ClassNotFoundException {
            NeuralNetwork netWork = NeuralNetwork.init(fileName);
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{1, 0})));
            LOGGER.info(Arrays.toString(netWork.getAnswer(new double[]{1, 1})));
        }
    }
}
